package com.spring.wjc.service;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

//此注解是为了让容器能扫描到AppConfig对象
@Configuration
@ComponentScan(basePackageClasses = {MyService.class})
public class AppConfig {
    //@Bean相当于<bean class="x.x.x.MyService"></bean>
    @Bean
    public MyService getMyService() {
        return new MyService();
    }

    public static void main(String[] args) {
        ApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);

        MyService service = ctx.getBean(MyService.class);
        service.sayHello("www");
    }
}