package boot.customer.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
//个性化设置
//@Import(CustomizeContainerConfig.class)
//配置jetty容器
@Import(JettyContainerConfig.class)
public class ContainerApp {
    public static void main(String[] args) {
        SpringApplication.run(ContainerApp.class,args);
    }
}
