package cloud.bus;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 依次启动eureka-center、confg-server,启动两个config-client，端口为：8881、8882。

 访问http://localhost:8881/hi 或者http://localhost:8882/hi 浏览器显示：
 foo version 3

 这时我们去代码仓库将foo的值改为“foo version 4”，即改变配置文件foo的值。如果是传统的做法，需要重启服务，才能达到配置文件的更新。
 此时，我们只需要发送post请求：http://localhost:8881/bus/refresh
 你会发现config-client会重新读取配置文件

 重新读取配置文件：
 这时我们再访问http://localhost:8881/hi 或者http://localhost:8882/hi 浏览器显示：
 foo version 4

 另外，/bus/refresh接口可以指定服务，即使用”destination”参数，比如 “/bus/refresh?destination=customers:**” 即刷新服务名为customers的所有服务，不管i
 */
@SpringBootApplication
@EnableEurekaClient
@RestController
@RefreshScope
public class BusConfigClientApp {

    public static void main(String[] args) {
        SpringApplication.run(BusConfigClientApp.class, args);
    }

    @Value("${foo}")
    private String foo;

    @RequestMapping(value = "/hi")
    public String hi(){
        return foo;
    }
}