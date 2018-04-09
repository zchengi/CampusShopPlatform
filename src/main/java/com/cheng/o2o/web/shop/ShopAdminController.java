package com.cheng.o2o.web.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 主要用来解析路由并转发到相应的 html 中
 *
 * @author cheng
 *         2018/3/29 23:28
 */
@Controller
@RequestMapping(value = "/shopadmin", method = RequestMethod.GET)
public class ShopAdminController {

    @GetMapping("/shopoperation")
    public String shopEdit() {
        // 转发至店铺注册编辑页面
        return "shop/shopoperation";
    }

    @GetMapping("/shoplist")
    public String shopList() {
        // 转发至店铺列表页面
        return "shop/shoplist";
    }

    @GetMapping("/shopmanagement")
    public String shopManagement() {
        // 转发至店铺管理页面
        return "shop/shopmanagement";
    }

    @GetMapping("/productcategorymanagement")
    public String productCategoryManagement() {
        // 转发至商品类别管理页面
        return "shop/productcategorymanagement";
    }

    @GetMapping("/productoperation")
    public String productOperation() {
        // 转发至商品添加/编辑页面
        return "shop/productoperation";
    }

    @GetMapping("/productmanagement")
    public String productManagement() {
        // 转发至商品管理页面
        return "shop/productmanagement";
    }
}
