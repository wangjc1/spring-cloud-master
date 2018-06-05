package cloud.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * 需要引入spring-cloud-starter-eureka包
 *
 * 测试：
 * 1. 先启动 RegistryApp 注册中心
 * 2. 启动服务提供方EurekaProviderApp
 * 3. 启动RibbonApp消费路由
 * 4. 访问http://localhost:8888/hi?name=Alex
 */
@SpringBootApplication
@EnableDiscoveryClient
@EnableHystrix
public class RibbonApp {

	@Bean
	@LoadBalanced
	RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(RibbonApp.class, args);
	}
}