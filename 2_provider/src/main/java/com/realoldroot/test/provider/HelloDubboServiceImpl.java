package com.realoldroot.test.provider;

import com.realoldroot.test.dubbo.HelloDubboService;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.stereotype.Service;

/**
 * @author zhengenshen@gmail.com
 * @date 2022/1/21 11:57
 */
@Service
@DubboService
public class HelloDubboServiceImpl implements HelloDubboService {

    @Override
    public String say() {
        return "Hello!";
    }
}
