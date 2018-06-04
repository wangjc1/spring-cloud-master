package cloud.eureka.web;

import cloud.eureka.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @Autowired
    private HelloService helloService;

    @RequestMapping(value = "/hi", method = RequestMethod.GET)
    public String hi(String name) {
        return helloService.hiService(name);
    }
}