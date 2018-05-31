package boot.customer.container;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Import;

@EnableAutoConfiguration
@Import(ContainerConfig.class)
public class ContainerApp {
    public static void main(String[] args) {
        SpringApplication.run(ContainerApp.class,args);
    }
}
