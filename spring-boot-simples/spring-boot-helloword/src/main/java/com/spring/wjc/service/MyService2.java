package com.spring.wjc.service;

import org.springframework.stereotype.Service;

/**
 * @author: wangjc
 * 2018/5/25
 */
@Service
public class MyService2 {
    public String sayHello(String name){
        return "MyService2 Hello "+name;
    }
}
