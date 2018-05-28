package com.spring.wjc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

/**
 * 相当于启动一个空的Tomcat，默认端口是8080
 */
@EnableAutoConfiguration
//@Configuration
//@ComponentScan
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }
}
