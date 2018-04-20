package com.cheng.o2o.web.shop;

import com.cheng.o2o.dto.ShopAuthMapExecution;
import com.cheng.o2o.entity.Shop;
import com.cheng.o2o.entity.ShopAuthMap;
import com.cheng.o2o.enums.ShopAuthMapStateEnum;
import com.cheng.o2o.service.ShopAuthMapService;
import com.cheng.o2o.util.CodeUtil;
import com.cheng.o2o.util.HttpServletRequestUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author cheng
 *         2018/4/20 20:18
 */
@Controller
@RequestMapping("/shopadmin")
public class ShopAuthManagementController {

    @Autowired
    private ShopAuthMapService shopAuthMapService;


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
}
