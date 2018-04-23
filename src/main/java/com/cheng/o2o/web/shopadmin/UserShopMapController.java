package com.cheng.o2o.web.shopadmin;

import com.cheng.o2o.dto.UserShopMapExecution;
import com.cheng.o2o.entity.PersonInfo;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.UserShopMap;
import com.cheng.o2o.service.UserShopMapService;
import com.cheng.o2o.util.HttpServletRequestUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/23 12:29
 */
@Controller
@RequestMapping("/shopadmin")
public class UserShopMapController {

    @Autowired
    private UserShopMapService userShopMapService;

    @GetMapping("/listusershopmapsbyshop")
    @ResponseBody
    private Map<String, Object> listUserShopMapsByShop(HttpServletRequest request) {

        Map<String, Object> modelMap = new HashMap<>(4);

        // 获取分页信息
        int pageIndex = HttpServletRequestUtil.getInt(request, "pageIndex");
        int pageSize = HttpServletRequestUtil.getInt(request, "pageSize");

        // 从session种获取当前店铺信息
        Shop currentShop = (Shop) request.getSession().getAttribute("currentShop");

        // 空值判断
        if (pageIndex > -1 && pageSize > -1 && currentShop != null && currentShop.getShopId() != null) {
            UserShopMap userShopMapCondition = new UserShopMap();
            // 传入查询条件
            userShopMapCondition.setShop(currentShop);
            String userName = HttpServletRequestUtil.getString(request, "userName");
            if (userName != null) {
                // 如果传入顾客名，则按照顾客名模糊查询
                PersonInfo customer = new PersonInfo();
                customer.setName(userName);
                userShopMapCondition.setUser(customer);
            }

            // 分页获取该店铺下的顾客积分列表
            UserShopMapExecution ue = userShopMapService.listUserShopMap(userShopMapCondition, pageIndex, pageSize);
            modelMap.put("userShopMapList", ue.getUserShopMapList());
            modelMap.put("count", ue.getCount());
            modelMap.put("success", true);
        } else {
            modelMap.put("success", false);
            modelMap.put("errMsg", "Empty pageSize or pageIndex or shopId");
        }

        return modelMap;
    }
}
