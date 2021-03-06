package cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 远程仓库https://github.com/wangjc1/cloud-config-repo/

 中有个文件文件中有两个属性：
 1. config-client-dev.properties
    foo = dev foo version 3
 2. config-client-pro.properties
    foo = pro foo version 3

 启动程序：访问http://localhost:8888/config-client/dev

 {"name":"foo","profiles":["dev"],"label":"master",
 "version":"792ffc77c03f4b138d28e89b576900ac5e01a44b","state":null,"propertySources":[]}

 证明配置服务中心可以从远程程序获取配置信息,http请求地址和资源文件映射如下:
 /{application}/{profile}[/{label}]
 /{application}-{profile}.yml
 /{label}/{application}-{profile}.yml
 /{application}-{profile}.properties
 /{label}/{application}-{profile}.properties

 注意：这里如果只是简单测试不要引入spring-cloud-starter-eureka包，否则会自动向配置中心注册，详细请参考高可用配置


 */
@SpringBootApplication
@EnableConfigServer
public class ConfigServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class, args);
    }
}