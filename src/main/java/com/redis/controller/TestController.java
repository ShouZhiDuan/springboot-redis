package com.redis.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Auther: ShouZhi@Duan
 * @Date: 2022/4/2 15:10
 * @Description:
 */
@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/do")
    public String test(String msg){
        return msg;
    }

}
