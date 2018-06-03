package boot.customer.container;

import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.jetty.JettyEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: admin
 * 2018/6/1
 */
@Configuration
public class JettyContainerConfig {
    @Bean
    public EmbeddedServletContainerFactory jettyEmbeddedServletContainerFactory222() {
        JettyEmbeddedServletContainerFactory container = new JettyEmbeddedServletContainerFactory();
        // Port Number
        container.setPort(8082);
        // Context Path
        container.setContextPath("/jetty");
        // Session Timeout
        container.setSessionTimeout(30);
        return container;
    }


}
