package boot.customer.enableAutoConfigureAnno;

import org.springframework.boot.SpringApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * https://www.jianshu.com/p/2a171fa80756
 *
 * @MyEnableAutoConfiguration注解简单点说就是用来启动Tomcat容器的
 *
 * EnableAutoConfigurationImportSelector说已经被废弃了，后面用它的父类代替
 */
@Configuration
@MyEnableAutoConfiguration
public class CustomizeEnableAutoConfigureApp {
    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CustomizeEnableAutoConfigureApp.class);
        application.run(args);
    }

    @Controller
    public static class MyController {
        @RequestMapping
        @ResponseBody
        public Map index() {
            Map<String, String> map = new HashMap<String, String>();
            map.put("hello", "world");
            return map;
        }
    }
}