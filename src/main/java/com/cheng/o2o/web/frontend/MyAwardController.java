package com.cheng.o2o.web.frontend;

import com.cheng.o2o.dto.UserAwardMapExecution;
import com.cheng.o2o.entity.*;
import com.cheng.o2o.enums.UserAwardMapStateEnum;
import com.cheng.o2o.service.AwardService;
import com.cheng.o2o.service.PersonInfoService;
import com.cheng.o2o.service.UserAwardMapService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.cheng.o2o.util.ShortNetAddressUtil;
import com.cheng.o2o.web.shopadmin.ShopAuthManagementController;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/23 16:11
 */
@Controller
@RequestMapping("/frontend")
public class MyAwardController {

    /**
     * 微信获取用户信息的api前缀
     */
    private static String urlPrefix;
    /**
     * 微信获取用户信息的api中间部分
     */
    private static String urlMiddle;
    /**
     * 微信获取用户信息的api后缀
     */
    private static String urlSuffix;
    /**
     * 微信回传给响应添加用户奖品映射信息的 url
     */
    private static String exchangeUrl;

    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private AwardService awardService;
    @Autowired
    private PersonInfoService personInfoService;

    /**
     * 在线兑换礼品
     *
     * @param request
     * @return
     */
    @PostMapping("/adduserawardmap")
    @ResponseBody
    private Map<String, Object> addUserAwardMap(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");
        Long awardId = HttpServletRequestUtil.getLong(request, "awardId");
        UserAwardMap userAwardMap = compactUserAwardMap4Add(user, awardId);

        if (userAwardMap != null) {
            try {
                UserAwardMapExecution ue = userAwardMapService.addUserAwardMap(userAwardMap);
                if (ue.getState() == UserAwardMapStateEnum.SUCCESS.getState()) {
                    modelMap.put("success", true);
                } else {
                    modelMap.put("success", false);
                    modelMap.put("errMsg", ue.getStateInfo());
                }
            } catch (RuntimeException e) {
                modelMap.put("success", false);
                modelMap.put("errMsg", e.toString());
                return modelMap;
            }
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "请选择领取的奖品!");
        }

        return modelMap;
    }


    /**
     * 根据顾客奖品映射id获取单条顾客奖品的映射信息
     *
     * @param request
     * @return
     */
    @GetMapping("/getawardbyuserawardid")
    @ResponseBody
    private Map<String, Object> getAwardByUserAwardId(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        // 空值判断
        if (userAwardId > -1) {
            // 根据id获取顾客奖品的映射信息，进而获取奖品id
            UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
            // 根据奖品id获取奖品信息
            Award award = awardService.getAwardById(userAwardMap.getAward().getAwardId());
            // 将奖品信息和领取状态返回给前端
            modelMap.put("award", award);
            modelMap.put("usedStatus", userAwardMap.getUsedStatus());
            modelMap.put("userAwardMap", userAwardMap);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("success", "Empty awardId");
        }

        return modelMap;
    }

    /**
     * 获取顾客的兑换列表
     *
     * @param request
     * @return
     */
    @GetMapping("/listuserawardmapsbycustomer")
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByCustomer(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != null) {
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            userAwardMapCondition.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId > -1) {
                // 如果店铺id不为空，则将其添加进查询条件
                // 即查询该用户在某个店铺的兑换信息
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userAwardMapCondition.setShop(shop);
            }
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 如果奖品名不为空，则讲其添加进查询条件进行模糊查询
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }

            // 根据传入的查询条件分页获取用户奖品映射信息
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ue.getUserAwardMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or userId");
        }

        return modelMap;
    }

    /**
     * 生成商品的消费凭证二维码，供操作员扫描，证明已领取
     *
     * @param request
     * @param response
     */
    @GetMapping("/generateqrcode4award")
    @ResponseBody
    private void generateQRCode4Award(HttpServletRequest request, HttpServletResponse response) {

        long userAwardId = HttpServletRequestUtil.getLong(request, "userAwardId");
        UserAwardMap userAwardMap = userAwardMapService.getUserAwardMapById(userAwardId);
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        // 空值判断
        if (userAwardId != -1 && user != null && user.getUserId() != null
                && userAwardMap.getUser().getUserId().equals(user.getUserId())) {
            long timeStamp = System.currentTimeMillis();

            String content = "{aaauserAwardIdaaa:" + userAwardId +
                    ",aaacustomerIdeaaa" + user.getUserId() + ",aaacreateTimeaaa:" + timeStamp + "}";
            try {
                // 将content的信息先进行base64编码以避免特殊字符造成的干扰，之后拼接目标 url
                String longUrl = urlPrefix + exchangeUrl + urlMiddle +
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
     * 组合查询条件，将条件封装到 userAwardMapCondition 对象里返回
     *
     * @param user
     * @param awardId
     * @return
     */
    private UserAwardMap compactUserAwardMap4Add(PersonInfo user, Long awardId) {

        UserAwardMap userAwardMapCondition = new UserAwardMap();
        if (user != null && user.getUserId() != null) {
            // 设置用户信息
            userAwardMapCondition.setUser(user);
            // 设置默认操作员为自己
            userAwardMapCondition.setOperator(user);
        }
        if (awardId != null) {
            Award award = awardService.getAwardById(awardId);
            Shop shop = new Shop();
            shop.setShopId(award.getShopId());

            // 设置奖品信息
            userAwardMapCondition.setAward(award);
            // 设置兑换所需积分
            userAwardMapCondition.setPoint(award.getPoint());
            // 设置店铺id
            userAwardMapCondition.setShop(shop);
        }

        return userAwardMapCondition;
    }

    @Value("${wechat.prefix}")
    public void setUrlPrefix(String urlPrefix) {
        MyAwardController.urlPrefix = urlPrefix;
    }

    @Value("${wechat.middle}")
    public void setUrlMiddle(String urlMiddle) {
        MyAwardController.urlMiddle = urlMiddle;
    }

    @Value("${wechat.suffix}")
    public void setUrlSuffix(String urlSuffix) {
        MyAwardController.urlSuffix = urlSuffix;
    }

    @Value("${wechat.auth.url}")
    public void setExchangeUrl(String exchangeUrl) {
        MyAwardController.exchangeUrl = exchangeUrl;
    }
}
