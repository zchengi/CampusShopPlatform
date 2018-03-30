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
@RequestMapping(value = "/shop", method = RequestMethod.GET)
public class AdminController {

    @GetMapping("shopedit")
    public String shopEdit() {
        return "shop/shopedit";
    }
}
