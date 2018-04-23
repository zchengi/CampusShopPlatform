package com.cheng.o2o.web.shopadmin;

import com.cheng.o2o.dto.ShopAuthMapExecution;
import com.cheng.o2o.dto.UserAccessToken;
import com.cheng.o2o.dto.UserAwardMapExecution;
import com.cheng.o2o.dto.WechatInfo;
import com.cheng.o2o.entity.*;
import com.cheng.o2o.enums.UserProductMapStateEnum;
import com.cheng.o2o.service.ProductService;
import com.cheng.o2o.service.ShopAuthMapService;
import com.cheng.o2o.service.UserAwardMapService;
import com.cheng.o2o.service.WechatAuthService;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.cheng.o2o.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/23 13:29
 */
@Controller
@RequestMapping("/shopadmin")
public class UserAwardMapController {

    @Autowired
    private UserAwardMapService userAwardMapService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @GetMapping("/listuserawardmapsbyshop")
    @ResponseBody
    private Map<String, Object> listUserAwardMapsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            UserAwardMap userAwardMapCondition = new UserAwardMap();
            // 传入查询条件
            userAwardMapCondition.setShop(currentShop);
            String awardName = HttpServletRequestUtil.getString(request, "awardName");
            if (awardName != null) {
                // 如果传入奖品名称，则按照顾奖品名模糊查询
                Award award = new Award();
                award.setAwardName(awardName);
                userAwardMapCondition.setAward(award);
            }

            // 分页获取该店铺下的顾客积分列表
            UserAwardMapExecution ue = userAwardMapService.listUserAwardMap(userAwardMapCondition, pageIndex, pageSize);
            modelMap.put("userAwardMapList", ue.getUserAwardMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or shopId");
        }

        return modelMap;
    }

    /**
     * 操作员扫顾客的奖品二维码派发奖品，证明顾客已领取奖品
     *
     * @param request
     * @param response
     * @return
     */
    @GetMapping("/exchangeaward")
    private String exchangeAward(HttpServletRequest request, HttpServletResponse response) {

        // 获取负责扫描二维码的店员信息
        WeChatAuth auth = getOperatorInfo(request);
        if (auth != null) {

            PersonInfo operator = auth.getPersonInfo();
            request.getSession().setAttribute("user", operator);

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

            // 获取用户奖品映射主键
            Long userAwardId = wechatInfo.getCustomerId();
            // 获取顾客id
            Long customerId = wechatInfo.getCustomerId();

            UserAwardMap userAwardMap = compactUserShopMap3Add(customerId, userAwardId, operator);

            if (userAwardMap != null && customerId != -1) {
                try {
                    if (!checkShopAuth(operator.getUserId(), userAwardMap)) {
                        return "shop/operationfail";
                    }
                    // 修改奖品领取状态
                    UserAwardMapExecution ue = userAwardMapService.modifyUserAwardMap(userAwardMap);
                    if (ue.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
                        return "shop/operationsuccess";
                    }
                } catch (RuntimeException e) {
                    return "shop/operationfail";
                }
            }
        }

        return "shop/operationfail";
    }

    /**
     * 根据code获取 userAccessToken ，进而获取微信用户信息
     *
     * @param request
     * @return
     */
    private WeChatAuth getOperatorInfo(HttpServletRequest request) {

        String code = request.getParameter("code");
        WeChatAuth auth = null;
        if (null != code) {
            UserAccessToken token;
            token = WechatUtil.getUserAccessToken(code);
            String openId = token.getOpenId();
            auth = wechatAuthService.getWechatAuthByOpenId(openId);
        }

        return auth;
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


    /**
     * 组建用户消费记录
     *
     * @param customerId
     * @param productId
     * @param operator
     * @return
     */
    private UserAwardMap compactUserShopMap3Add(Long customerId, Long productId, PersonInfo operator) {

        UserAwardMap userAwardMap = null;
        if (customerId != null && productId != null) {
            userAwardMap = new UserAwardMap();

            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);
            Product product = productService.getProductById(productId);

            userAwardMap.setShop(product.getShop());
            userAwardMap.setUser(customer);
            userAwardMap.setOperator(operator);
            userAwardMap.setPoint(product.getPoint());
            userAwardMap.setCreateTime(new Date());
        }

        return userAwardMap;
    }

    /**
     * 检查扫码人员是否有操作权限
     *
     * @param userId
     * @param userAwardMap
     * @return
     */
    private boolean checkShopAuth(Long userId, UserAwardMap userAwardMap) {

        // 获取该店铺的所有授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.
                listShopAuthMapByShopId(userAwardMap.getShop().getShopId(), 0, 999);

        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            // 查看当前人员是否有权限授权
            if (shopAuthMap.getEmployee().getUserId().equals(userId)) {
                return true;
            }
        }

        return false;
    }
}

