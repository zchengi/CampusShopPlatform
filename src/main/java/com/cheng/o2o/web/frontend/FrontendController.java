package com.cheng.o2o.web.frontend;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author cheng
 *         2018/4/10 22:56
 */
@Controller
@RequestMapping("/frontend")
public class FrontendController {

    /**
     * 用户访问首页路由
     */
    @GetMapping("/index")
    private String index() {
        return "frontend/index";
    }

    /**
     * 商品列表页路由
     */
    @GetMapping("/shoplist")
    private String showShopList() {
        return "frontend/shoplist";
    }

    /**
     * 店铺详情页路由
     */
    @GetMapping("/shopdetail")
    private String showShopDetail() {
        return "frontend/shopdetail";
    }

    /**
     * 商品详情页路由
     */
    @GetMapping("/productdetail")
    private String showProductDetail() {
        return "frontend/productdetail";
    }

    /**
     * 奖品列表页路由
     */
    @GetMapping("/awardlist")
    private String showAwardList() {
        return "frontend/awardlist";
    }

    /**
     * 奖品兑换页路由
     */
    @GetMapping("/pointrecord")
    private String showPointRecord() {
        return "frontend/pointrecord";
    }

    /**
     * 消费记录列表页路由
     */
    @GetMapping("/myrecord")
    private String showMyRecord() {
        return "frontend/myrecord";
    }

    /**
     * 消费记录列表页路由
     */
    @GetMapping("/mypoint")
    private String showMyPoint() {
        return "frontend/mypoint";
    }
    /**
     * 奖品详情页路由
     */
    @GetMapping("/myawarddetail")
    private String showMyAwardDetail() {
        return "frontend/myawarddetail";
    }
}
