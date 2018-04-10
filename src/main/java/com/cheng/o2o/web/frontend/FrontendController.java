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

    @GetMapping("/index")
    public String index() {
        return "frontend/index";
    }
}
