package cloud.zipkin.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.sampler.AlwaysSampler;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class FirstServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(FirstServiceApp.class, args);
    }

    private static final Logger LOG = Logger.getLogger(FirstServiceApp.class.getName());


    @Autowired
    private RestTemplate restTemplate;

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }

    @RequestMapping("/second")
    public String callHome(){
        LOG.log(Level.INFO, "calling trace service-second  ");
        return restTemplate.getForObject("http://localhost:8989/hi", String.class);
    }
    @RequestMapping("/hi")
    public String info(){
        LOG.log(Level.INFO, "calling trace service-first ");

        return "i'm service-first";
    }

    @Bean
    public AlwaysSampler defaultSampler(){
        return new AlwaysSampler();
    }
}