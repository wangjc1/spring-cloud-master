package com.spring.boot.listener;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Spring boot 启动后广播事件监听
 * @see  EventPublishingRunListener
 * @see  spring-boot-1.5.9.RELEASE.jar!\META-INF\spring.factories
 */
public class MyStartListner implements SpringApplicationRunListener {

    public MyStartListner(SpringApplication application, String[] args) {
    }

    @Override
    public void starting() {
        System.out.println("我的程序启动啦！！！");
    }

    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) {
    }

    @Override
    public void contextPrepared(ConfigurableApplicationContext context) {

    }

    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {

    }

    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
    }
}