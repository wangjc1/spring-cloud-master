package cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 远程仓库https://github.com/wangjc1/cloud-config-repo/

 中有个文件文件中有两个属性：
 1. config-ha-client-dev.properties
    foo = HA dev foo version 3
 2. config-ha-client-pro.properties
    foo = HA pro foo version 3

 高可用配置和普通服务的区别是读取配置文件不再写ip地址，而是服务名，这时如果配置服务部署多份，通过负载均衡，从而高可用。
 依次启动spring-eureka-center,spring-config-server,spring-config-client 三个服务


 启动程序：访问http://localhost:8888/config-ha-client/dev

 {"name":"foo","profiles":["dev"],"label":"master",
 "version":"792ffc77c03f4b138d28e89b576900ac5e01a44b","state":null,"propertySources":[]}

 证明配置服务中心可以从远程程序获取配置信息,http请求地址和资源文件映射如下:
 /{application}/{profile}[/{label}]
 /{application}-{profile}.yml
 /{label}/{application}-{profile}.yml
 /{application}-{profile}.properties
 /{label}/{application}-{profile}.properties


 */
@SpringBootApplication
@EnableConfigServer
public class ConfigHAServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigHAServerApp.class, args);
    }
}