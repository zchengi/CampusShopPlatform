package com.cheng.o2o.web.shop;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author cheng
 *         2018/3/29 23:28
 */
@Controller
@RequestMapping(value = "/shopadmin", method = RequestMethod.GET)
public class ShopAdminController {

    @GetMapping("shopoperation")
    public String shopOperation() {
        return "shop/shopoperation";
    }
}
