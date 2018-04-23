package com.cheng.o2o.web.shopadmin;

import com.cheng.o2o.dto.*;
import com.cheng.o2o.entity.*;
import com.cheng.o2o.enums.UserProductMapStateEnum;
import com.cheng.o2o.service.*;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.cheng.o2o.util.wechat.WechatUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author cheng
 *         2018/4/22 15:09
 */
@Controller
@RequestMapping("/shopadmin")
public class UserProductManagementController {

    @Autowired
    private UserProductMapService userProductMapService;
    @Autowired
    private ProductSellDailyService productSellDailyService;
    @Autowired
    private WechatAuthService wechatAuthService;
    @Autowired
    private ProductService productService;
    @Autowired
    private ShopAuthMapService shopAuthMapService;

    @GetMapping("/adduserproductmap")
    @ResponseBody
    private String addUserProductMap(HttpServletRequest request) {

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

            // 获取添加消费记录所需的参数并组成 userProductMap 实例
            Long productId = wechatInfo.getProductId();
            Long customerId = wechatInfo.getCustomerId();
            UserProductMap userProductMap = compactUserShopMap3Add(customerId, productId, auth.getPersonInfo());

            if (userProductMap != null && customerId != -1) {
                try {
                    if (!checkShopAuth(operator.getUserId(), userProductMap)) {
                        return "shop/operationfail";
                    }
                    // 添加消费记录
                    UserProductMapExecution se = userProductMapService.addUserProductMap(userProductMap);
                    if (se.getState() == UserProductMapStateEnum.SUCCESS.getState()) {
                        return "shop/operationsuccess";
                    }
                } catch (RuntimeException e) {
                    return "shop/operationfail";
                }
            }
        }

        return "shop/operationfail";
    }

    @GetMapping("/listuserproductmapsbyshop")
    @ResponseBody
    private Map<String, Object> listUserProductMapsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");
        // 获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值校验
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            // 添加查询条件
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setShop(currentShop);
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                // 如果传入商品名模糊查询，则传入 productName
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }

            UserProductMapExecution ue = userProductMapService.
                    listUserProductMap(userProductMapCondition, pageIndex, pageSize);

            modelMap.put("userProductMapList", ue.getUserProductMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("success", "Empty pageSize or pageIndex or shopId");
        }

        return modelMap;
    }

    @GetMapping("/listproductselldailyinfobyshop")
    @ResponseBody
    private Map<String, Object> listProductSellDailyInfoByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);
        // 获取当前的店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");
        // 空值校验，确保shopId不为空
        if (currentShop != null && currentShop.getShopId() != null) {
            // 添加查询条件
            ProductSellDaily productSellDailyCondition = new ProductSellDaily();
            productSellDailyCondition.setShop(currentShop);

            Calendar calendar = Calendar.getInstance();
            // 获取昨天的日期
            calendar.add(Calendar.DATE, -1);
            Date endTime = calendar.getTime();
            // 获取七天前的日期
            calendar.add(Calendar.DATE, -6);
            Date beginTime = calendar.getTime();
            // 根据传入的查询条件获取该店铺的商品销售情况
            List<ProductSellDaily> productSellDailyList = productSellDailyService.
                    listProductSellDaily(productSellDailyCondition, beginTime, endTime);

            // 指定日期的格式
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            // 商品名列表，保证唯一性
            HashSet<String> legendData = new HashSet<>();
            // x轴数据
            TreeSet<String> xData = new TreeSet<>();
            // 定义 series
            List<EchartSeries> series = new ArrayList<>();
            // 日销量列表
            List<Integer> totalList = new ArrayList<>();

            // 当前商品名，默认为空
            String currentProductName = "";
            for (int i = 0; i < productSellDailyList.size(); i++) {
                ProductSellDaily productSellDaily = productSellDailyList.get(i);
                // 自动去重
                legendData.add(productSellDaily.getProduct().getProductName());
                xData.add(sdf.format(productSellDaily.getCreateTime()));

                // 判断当前商品名是否与上一个商品名一致
                if (!currentProductName.equals(productSellDaily.getProduct().getProductName()) &&
                        !currentProductName.isEmpty()) {
                    // 如果 currentProductName 不等于获取的商品名，或者已遍历到列表的末尾，且 currentProductName 不为空
                    // 则是遍历到下一个商品的日销量信息了，将前一轮遍历的信息放入 series 中，
                    // 包括了商品名以及与商品对应的统计日期以及当日销量
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);

                    // 重置 totalList
                    totalList = new ArrayList<>();
                    // 变换下 currentProductId 为当前的 productId
                    currentProductName = productSellDaily.getProduct().getProductName();
                    // 继续添加新的值
                    totalList.add(productSellDaily.getTotal());
                } else {
                    // 如果还是当前的 productId 则继续添加新值
                    totalList.add(productSellDaily.getTotal());
                    currentProductName = productSellDaily.getProduct().getProductName();
                }

                // 队列末尾，需要将最后的一个商品销量信息也添加上
                if (i == productSellDailyList.size() - 1) {
                    EchartSeries es = new EchartSeries();
                    es.setName(currentProductName);
                    es.setData(totalList.subList(0, totalList.size()));
                    series.add(es);
                }
            }
            modelMap.put("series", series);
            modelMap.put("legendData", legendData);

            // 拼接出 xAis
            List<EchartXAxis> xAxis = new ArrayList<>();
            EchartXAxis exa = new EchartXAxis();
            exa.setData(xData);
            xAxis.add(exa);
            modelMap.put("xAxis", xAxis);
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty shopId");
        }

        return modelMap;
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
     * @param personInfo
     * @return
     */
    private UserProductMap compactUserShopMap3Add(Long customerId, Long productId, PersonInfo personInfo) {

        UserProductMap userProductMap = null;
        if (customerId != null && productId != null) {
            userProductMap = new UserProductMap();

            PersonInfo customer = new PersonInfo();
            customer.setUserId(customerId);

            // 主要为了获取商品积分
            Product product = productService.getProductById(productId);
            userProductMap.setProduct(product);
            userProductMap.setShop(product.getShop());
            userProductMap.setUser(customer);
            userProductMap.setPoint(product.getPoint());
            userProductMap.setCreateTime(new Date());
        }

        return userProductMap;
    }

    /**
     * 检查扫码人员是否有操作权限
     *
     * @param userId
     * @param userProductMap
     * @return
     */
    private boolean checkShopAuth(Long userId, UserProductMap userProductMap) {

        // 获取该店铺的所有授权信息
        ShopAuthMapExecution shopAuthMapExecution = shopAuthMapService.
                listShopAuthMapByShopId(userProductMap.getShop().getShopId(), 0, 999);

        for (ShopAuthMap shopAuthMap : shopAuthMapExecution.getShopAuthMapList()) {
            // 查看当前人员是否有权限授权
            if (shopAuthMap.getEmployee().getUserId().equals(userId)) {
                return true;
            }
        }

        return false;
    }
}
