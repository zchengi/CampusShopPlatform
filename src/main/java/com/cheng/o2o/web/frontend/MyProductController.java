package com.cheng.o2o.web.frontend;

import com.cheng.o2o.dto.UserAwardMapExecution;
import com.cheng.o2o.dto.UserProductMapExecution;
import com.cheng.o2o.entity.*;
import com.cheng.o2o.service.UserProductMapService;
import com.cheng.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/23 21:40
 */
@Service
@RequestMapping("/frontend")
public class MyProductController {

    @Autowired
    public UserProductMapService userProductMapService;

    /**
     * 列出某个顾客的商品消费信息
     *
     * @param request
     * @return
     */
    @GetMapping("/listuserproductmapsbycustomer")
    @ResponseBody
    private Map<String, Object> listUserProductMapsByCustomer(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != null) {
            UserProductMap userProductMapCondition = new UserProductMap();
            userProductMapCondition.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            if (shopId > -1) {
                // 如果店铺id不为空，则将其添加进查询条件
                Shop shop = new Shop();
                shop.setShopId(shopId);
                userProductMapCondition.setShop(shop);
            }
            String productName = HttpServletRequestUtil.getString(request, "productName");
            if (productName != null) {
                // 如果商品名不为空，则讲其添加进查询条件进行模糊查询
                Product product = new Product();
                product.setProductName(productName);
                userProductMapCondition.setProduct(product);
            }

            // 根据传入的查询条件分页获取用户商品映射信息
            UserProductMapExecution ue = userProductMapService.listUserProductMap(userProductMapCondition, pageIndex, pageSize);
            modelMap.put("userProductMapList", ue.getUserProductMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or userId");
        }

        return modelMap;
    }
}
