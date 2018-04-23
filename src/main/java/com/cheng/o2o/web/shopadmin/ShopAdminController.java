package com.cheng.o2o.web.shopadmin;

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
    private String shopEdit() {
        // 转发至店铺注册编辑页面
        return "shop/shopoperation";
    }

    @GetMapping("/shoplist")
    private String shopList() {
        // 转发至店铺列表页面
        return "shop/shoplist";
    }

    @GetMapping("/shopmanagement")
    private String shopManagement() {
        // 转发至店铺管理页面
        return "shop/shopmanagement";
    }

    @GetMapping("/productcategorymanagement")
    private String productCategoryManagement() {
        // 转发至商品类别管理页面
        return "shop/productcategorymanagement";
    }

    @GetMapping("/productmanagement")
    private String productManagement() {
        // 转发至商品管理页面
        return "shop/productmanagement";
    }

    @GetMapping("/productoperation")
    private String productOperation() {
        // 转发至商品添加/编辑页面
        return "shop/productoperation";
    }

    @GetMapping("/shopauthmanagement")
    private String shopAuthManagement() {
        // 转发至店铺授权页面
        return "shop/shopauthmanagement";
    }

    @GetMapping("/shopauthoperation")
    private String shopAuthOperation() {
        // 转发至店铺授权信息修改页面
        return "shop/shopauthoperation";
    }

    @GetMapping("/productbuycheck")
    private String productBuyCheck() {
        // 转发至店铺的消费记录的页面
        return "shop/productbuycheck";
    }

    @GetMapping("/usershopcheck")
    private String userShopCheck() {
        // 转发至用户积分统计页面
        return "shop/usershopcheck";
    }

    @GetMapping("/awarddelivercheck")
    private String awardDeliverCheck() {
        // 转发至用户积分兑换页面
        return "shop/awarddelivercheck";
    }


    @GetMapping("/awardmanagement")
    private String awardManagement() {
        // 转发至奖品管理页面
        return "shop/awardmanagement";
    }

    @GetMapping("/awardoperation")
    private String awardOperation() {
        // 转发至奖品添加/编辑页面
        return "shop/awardoperation";
    }
}
