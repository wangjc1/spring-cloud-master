package cloud.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 打开网址访问：http://localhost:8881/hi，网页显示：
 foo version 3

 比普通配置服务多了配置中心的几个参数：
 #配置中心
 eureka.client.serviceUrl.defaultZone=http://localhost:8761/eureka/
 #启用从配置中心读取文件。
 spring.cloud.config.discovery.enabled=true
 # 配置服务的servieId，即应用名。
 spring.cloud.config.discovery.serviceId=config-ha-server

 */
@SpringBootApplication
@RestController
public class ConfigHAClientApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigHAClientApp.class, args);
    }

    @Value("${foo}")
    private String foo;

    @RequestMapping(value = "/hi")
    public String hi(){
        return foo;
    }
}