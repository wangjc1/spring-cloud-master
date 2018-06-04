package cloud.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 远程仓库https://github.com/wangjc1/cloud-config-repo/

 中有个文件config-client-dev.properties文件中有一个属性：
 foo = foo version 3

 启动程序：访问http://localhost:9999/foo/dev

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
public class ConfigServerApp {

    public static void main(String[] args) {
        SpringApplication.run(ConfigServerApp.class, args);
    }
}