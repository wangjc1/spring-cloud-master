package com.spring.wjc.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@EnableAutoConfiguration
@RestController
public class ProfileController {

    @Value("${db.name}")
    private String dbName;

    @RequestMapping("/info")
    private Map hello() {
        Map map = new HashMap<>();
        map.put("db_name", dbName);
        return map;
    }

    public static void main(String[] args) {
        SpringApplication.run(ProfileController.class);
    }
}

