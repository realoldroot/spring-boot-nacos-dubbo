package com.realoldroot.test.consumer;

import com.realoldroot.test.dubbo.HelloDubboService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/1/21 11:57
 */

@RestController
public class HelloController {

    @DubboReference
    private HelloDubboService helloDubboService;


    @RequestMapping("/sayHello")
    public String sayHello() {
        return helloDubboService.say();
    }
}
