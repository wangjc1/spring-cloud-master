package com.spring.wjc.controller;

import com.spring.wjc.service.AppConfig;
import com.spring.wjc.service.MyService;
import com.spring.wjc.service.MyService2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableAutoConfiguration
@RestController
@Import(AppConfig.class)
public class HelloController {

    @Autowired
    private MyService service;//通过@Bean加载

    @Autowired
    private MyService2 service2;//通过@ComponentScan扫描@Service加载

    @RequestMapping("/hello")
    private Map hello() {
        Map map = new HashMap<>();
        map.put("say", service.sayHello("alex"));
        map.put("say2", service2.sayHello("alex"));
        return map;
    }

    public static void main(String[] args) {
        SpringApplication.run(HelloController.class);
    }
}

