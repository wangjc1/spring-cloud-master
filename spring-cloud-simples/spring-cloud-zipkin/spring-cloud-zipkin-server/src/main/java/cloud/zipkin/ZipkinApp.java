package cloud.zipkin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import zipkin.server.EnableZipkinServer;

/**
 * 简单例子，不持久化
 * http://localhost:8988/hi
 * 参考： https://blog.csdn.net/forezp/article/details/70162074
 */
@SpringBootApplication
@EnableZipkinServer
public class ZipkinApp {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinApp.class, args);
    }
}