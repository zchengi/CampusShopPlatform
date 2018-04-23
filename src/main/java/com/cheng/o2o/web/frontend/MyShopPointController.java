package com.cheng.o2o.web.frontend;

import com.cheng.o2o.dto.UserProductMapExecution;
import com.cheng.o2o.dto.UserShopMapExecution;
import com.cheng.o2o.entity.*;
import com.cheng.o2o.service.UserShopMapService;
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
 *         2018/4/23 21:58
 */
@Service
@RequestMapping("/frontend")
public class MyShopPointController {

    @Autowired
    public UserShopMapService userShopMapService;

    /**
     * 列出用户积分情况
     *
     * @param request
     * @return
     */
    @GetMapping("/listusershopmapsbycustomer")
    @ResponseBody
    private Map<String, Object> listUserShopMapsByCustomer(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前用户信息
        PersonInfo user = (PersonInfo) request.getSession().getAttribute("user");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && user != null && user.getUserId() != null) {
            UserShopMap userProductMapCondition = new UserShopMap();
            userProductMapCondition.setUser(user);
            long shopId = HttpServletRequestUtil.getLong(request, "shopId");
            Shop shop = new Shop();
            if (shopId > -1) {
                // 如果店铺id不为空，则将其添加进查询条件
                shop.setShopId(shopId);
            }
            String shopName = HttpServletRequestUtil.getString(request, "shopName");
            if (shopName != null) {
                shop.setShopName(shopName);
            }

            userProductMapCondition.setShop(shop);

            // 根据传入的查询条件分页获取用户商品映射信息
            UserShopMapExecution ue = userShopMapService.listUserShopMap(userProductMapCondition, pageIndex, pageSize);
            modelMap.put("userShopMapList", ue.getUserShopMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or userId");
        }

        return modelMap;
    }
}
