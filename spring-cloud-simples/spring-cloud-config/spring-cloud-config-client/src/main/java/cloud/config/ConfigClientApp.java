package cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 打开网址访问：http://localhost:8881/hi，网页显示：
 foo version 3

 注意：这里如果只是简单测试不要引入spring-cloud-starter-eureka包，否则会自动向配置中心寻找配置服务，会提示找不到 @Value("${foo}")


 */
@SpringBootApplication
@RestController
public class ConfigClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigClientApp.class, args);
    }

    @Value("${foo}")
    private String foo;

    @RequestMapping(value = "/hi")
    public String hi(){
        return foo;
    }
}