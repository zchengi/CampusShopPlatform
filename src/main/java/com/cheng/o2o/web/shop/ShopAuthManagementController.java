package com.cheng.o2o.web.shop;

import com.cheng.o2o.dto.ShopAuthMapExecution;
import com.cheng.o2o.dto.UserAccessToken;
import com.cheng.o2o.dto.WechatInfo;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.ShopAuthMap;
import com.cheng.o2o.entity.WeChatAuth;
import com.cheng.o2o.enums.ShopAuthMapStateEnum;
import com.cheng.o2o.service.PersonInfoService;
import com.cheng.o2o.service.ShopAuthMapService;
import com.cheng.o2o.service.WechatAuthService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.cheng.o2o.util.ShortNetAddressUtil;
import com.cheng.o2o.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/20 20:18
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {


    /**
     * 微信获取用户信息的api前缀
     */
    private String urlPrefix = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx0db09a35eac8319e&redirect_uri=";
    /**
     * 微信获取用户信息的api中间部分
     */
    private String urlMiddle = "&role_type=1&response_type=code&scope=snsapi_userinfo&state=";
    /**
     * 微信获取用户信息的api后缀
     */
    private String urlSuffix = "#wechat_redirect";
    /**
     * 微信回传给响应添加授权信息的 url
     */
    private String authUrl = "http://193.112.56.145/o2o/shopadmin/addshopauthmap";

    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @Autowired
    private PersonInfoService personInfoService;

    @Autowired
    private WechatAuthService wechatAuthService;

    @PostMapping("/modifyshopauthmap")
    @ResponseBody
    private Map<String, Object> modifyShopAuthMap(String shopAuthMapStr, HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 是授权编辑时候调用还是删除/恢复授权操作的时候调用
        // 若为前者则进行验证码判断，后者则跳过验证码判断
        boolean statusChange = HttpServletRequestUtil.getBoolean(request, "statusChange");
        // 验证码校验
        if (!statusChange && !CodeUtil.checkVerifyCode(request)) {
            modelMap.put("success", false);
            modelMap.put("errMsg", "验证码有误!");
            return modelMap;
        }

        ObjectMapper mapper = new ObjectMapper();
        ShopAuthMap shopAuthMap;
        try {
            // 将前台传入的字符串 json 转换成 shopAuthMap 实例
            shopAuthMap = mapper.readValue(shopAuthMapStr, ShopAuthMap.class);
        } catch (IOException e) {
            modelMap.put("success", false);
            modelMap.put("errMsg", e.toString());
            return modelMap;
        }

        // 空值判断
        if (shopAuthMap != null && shopAuthMap.getShopAuthId() != null) {
            try {
                // 查看被操作的员工是否是店家本身，店家本身不支持修改
                if (!checkPermission(shopAuthMap.getShopAuthId())) {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", "无法对店家本身权限做操作(已经是店铺的最高权限)1");
                    return modelMap;
                }

                ShopAuthMapExecution se = shopAuthMapService.modifyShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请输入要修改的授权信息!");
        }
        return modelMap;
    }

    @GetMapping("/listshopauthmapsbyshop")
    @ResponseBody
    private Map<String, Object> listShopAuthMapsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 取出分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 从session中获取店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && (currentShop != null) && (currentShop.getShopId() != null)) {
            // 分页取出该店铺下面的授权信息列表
            ShopAuthMapExecution se = shopAuthMapService.
                    listShopAuthMapByShopId(currentShop.getShopId(), pageIndex, pageSize);

            modelMap.put("success", true);
            modelMap.put("shopAuthMapList", se.getShopAuthMapList());
            modelMap.put("count", se.getCount());
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or shopId");
        }

        return modelMap;
    }

    @GetMapping("/getshopauthmapbyid")
    @ResponseBody
    private Map<String, Object> getShopAuthMapById(@RequestParam Long shopAuthId) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 空值判断
        if (shopAuthId != null && shopAuthId > -1) {
            // 根据前台传入的shopAuthId查找对应的授权信息
            ShopAuthMap shopAuthMap = shopAuthMapService.getShopAuthMapById(shopAuthId);
            modelMap.put("success", true);
            modelMap.put("shopAuthMap", shopAuthMap);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty shopAuthId");
        }

        return modelMap;
    }


    /**
     * 生成带有 url 的二维码，微信扫一扫就能链接到对应的 url 里面
     *
     * @param request
     * @param response
     */
    @GetMapping("/generateqrcode4shopauth")
    @ResponseBody
    private void generateQRCode4ShopAuth(HttpServletRequest request, HttpServletResponse response) {

        // 从session种获取当前shop信息
        Shop shop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值判断
        if (shop != null && shop.getShopId() != null) {
            // 获取当前时间戳，以保证二维码的在有效时间内，精确到毫秒
            long timeStamp = System.currentTimeMillis();
            // 将店铺id和timestamp传入content，赋值到state种，这样微信获取到这些信息后会回传到授权信息的添加方法里
            // 加上 "aaa" 是为了一会的在添加信息的方法里替换这些信息使用
            String content = "{aaashopIdaaa:" + shop.getShopId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            try {
                // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标 url
                String longUrl = urlPrefix + authUrl + urlMiddle +
                        URLEncoder.encode(content, "UTF-8") + urlSuffix;
                // 将目标url转换成短的url
                String shortUrl = ShortNetAddressUtil.getShortURL(longUrl);
                // 调用二维码生成的工具类方法，传入短的 url，生成二维码
                BitMatrix qRCodeImg = CodeUtil.generateQRCodeStream(shortUrl, response);
                // 将二维码以图片流的形式输出到前端
                MatrixToImageWriter.writeToStream(qRCodeImg, "png", response.getOutputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 根据微信回传回来的参数添加店铺的授权信息
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/addshopauthmap")
    public String addShopAuthMap(HttpServletRequest request, HttpServletResponse response) {

        // 从request中获取微信用户的信息
        WeChatAuth auth = getEmployeeInfo(request);
        if (auth != null) {
            // 根据userId获取用户信息
            PersonInfo user = personInfoService.getPersonInfoById(auth.getPersonInfo().getUserId());
            // 将完整的用户信息添加到user中
            request.getSession().setAttribute("user", user);

            WechatInfo wechatInfo;
            try {
                // 解析微信回传过来的自定义参数state，由于之前进行了编码，所以这里需要解码
                String qrCodeInfo = URLDecoder.decode(
                        HttpServletRequestUtil.getString(request, "state"), "UTF-8");

                ObjectMapper mapper = new ObjectMapper();
                // 将解码后的内容用aaa去替换掉之前生成二维码的时候加入的aaa，转换成WechatInfo实体类
                wechatInfo = mapper.readValue(qrCodeInfo.replace("aaa", "\""), WechatInfo.class);
            } catch (Exception e) {
                return "shop/operationfail";
            }

            // 检验二维码是否已经过期
            if (!checkQRCodeInfo(wechatInfo)) {
                return "shop/operationfail";
            }

            // 去重校验
            // 获取该店铺下所有的授权信息
            ShopAuthMapExecution allMapList = shopAuthMapService.
                    listShopAuthMapByShopId(wechatInfo.getShopId(), 0, 999);
            List<ShopAuthMap> shopAuthMapList = allMapList.getShopAuthMapList();
            for (ShopAuthMap shopAuthMap : shopAuthMapList) {
                if (shopAuthMap.getEmployee().getUserId().equals(user.getUserId())) {
                    return "shop/operationfail";
                }
            }

            try {
                // 根据获取到的内容，添加微信授权信息
                ShopAuthMap shopAuthMap = new ShopAuthMap();
                Shop shop = new Shop();
                shop.setShopId(wechatInfo.getShopId());
                shopAuthMap.setShop(shop);

                shopAuthMap.setEmployee(user);
                shopAuthMap.setTitle("员工");
                shopAuthMap.setTitleFlag(1);

                ShopAuthMapExecution se = shopAuthMapService.addShopAuthMap(shopAuthMap);
                if (se.getState() == ShopAuthMapStateEnum.SUCCESS.getState()) {
                    return "shop/operationsuccess";
                } else {
                    return "shop/operationfail";
                }
            } catch (RuntimeException e) {
                return "shop/operationfail";
            }
        } else {
            return "shop/operationfail";
        }
    }

    /**
     * 根据微信回传的code获取用户信息
     *
     * @param request
     * @return
     */
    private WeChatAuth getEmployeeInfo(HttpServletRequest request) {

        String code = request.getParameter("code");
        WeChatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            token = WechatUtil.getUserAccessToken(code);
            String openId = token.getOpenId();
            request.getSession().setAttribute("openId", openId);
            auth = wechatAuthService.getWechatAuthByOpenId(openId);
        }

        return auth;
    }

    /**
     * 检查被操作的对象是否可修改
     *
     * @param shopAuthId
     * @return
     */
    private boolean checkPermission(Long shopAuthId) {

        ShopAuthMap grantedPerson = shopAuthMapService.getShopAuthMapById(shopAuthId);
        // 如果为店铺老板本身，不能操作
        return grantedPerson.getTitleFlag() != 0;
    }

    /**
     * 根据二维码携带的createTime判断其是否超过了10分钟，超过10分钟则认为过期
     *
     * @param wechatInfo
     * @return
     */
    private boolean checkQRCodeInfo(WechatInfo wechatInfo) {

        if (wechatInfo != null && wechatInfo.getShopId() != null && wechatInfo.getCreateTime() != null) {
            long nowTime = System.currentTimeMillis();
            return (nowTime - wechatInfo.getCreateTime()) <= 600000;
        } else {
            return false;
        }
    }
}
