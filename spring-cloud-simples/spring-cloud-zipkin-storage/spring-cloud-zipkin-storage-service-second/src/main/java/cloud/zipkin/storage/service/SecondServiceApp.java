package cloud.zipkin.storage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.logging.Level;
import java.util.logging.Logger;

@SpringBootApplication
@RestController
public class SecondServiceApp {

    public static void main(String[] args) {
        SpringApplication.run(SecondServiceApp.class, args);
    }

    private static final Logger LOG = Logger.getLogger(SecondServiceApp.class.getName());


    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping("/first")
    public String callHome(){
        LOG.log(Level.INFO, "calling trace service-first  ");
        return restTemplate.getForObject("http://localhost:8988/hi", String.class);
    }
    @RequestMapping("/hi")
    public String info(){
        LOG.log(Level.INFO, "calling trace service-second ");

        return "i'm service-second";
    }

    @Bean
    public RestTemplate getRestTemplate(){
        return new RestTemplate();
    }
}