package boot.actuator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * 在生产环境中，需要实时或定期监控服务的可用性。spring-boot 的actuator（监控）功能提供了很多监控所需的接口。
 * 通过实现这些接口就可以自定义一些监控信息
 */
@SpringBootApplication
@EnableScheduling
public class MySpringBootApplication implements HealthIndicator {
    private static final Logger logger = LoggerFactory.getLogger(MySpringBootApplication.class);

    public static void main(String[] args) {  
        SpringApplication.run(MySpringBootApplication.class, args);
        logger.info("My Spring Boot Application Started");  
    }  
  
    /** 
     * 在/health接口调用的时候，返回多一个属性："mySpringBootApplication":{"status":"UP","hello":"world"}
     * http://localhost:54001/health
     */  
    @Override  
    public Health health() {
        return Health.up().withDetail("hello", "world").build();  
    }  
}