package com.cheng.o2o.web.shopadmin;

import com.cheng.o2o.dto.EchartSeries;
import com.cheng.o2o.dto.EchartXAxis;
import com.cheng.o2o.dto.UserProductMapExecution;
import com.cheng.o2o.entity.Product;
import com.cheng.o2o.entity.ProductSellDaily;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.UserProductMap;
import com.cheng.o2o.service.ProductSellDailyService;
import com.cheng.o2o.service.UserProductMapService;
import com.cheng.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
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
}
