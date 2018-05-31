package boot.customer.container;

import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
public class ContainerConfig {  
    @Bean
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer() {  
        return new EmbeddedServletContainerCustomizer() {
            @Override  
            public void customize(ConfigurableEmbeddedServletContainer container) {
                // Port Number  
                container.setPort(8081);  
                // Context Path  
                container.setContextPath("/springboot");  
                // Session Timeout  
                container.setSessionTimeout(30, TimeUnit.MINUTES);
            }  
        };  
     }  
}  
