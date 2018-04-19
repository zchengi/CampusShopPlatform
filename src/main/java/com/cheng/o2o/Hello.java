package com.cheng.o2o;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cheng
 *         2018/4/19 16:17
 */
@RestController
public class Hello {

    @GetMapping("/hello")
    public String hello() {
        return "Hello Spring Boot";
    }
}
