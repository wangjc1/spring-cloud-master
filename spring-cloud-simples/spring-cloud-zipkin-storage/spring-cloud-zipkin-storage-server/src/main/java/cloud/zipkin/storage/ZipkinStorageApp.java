package cloud.zipkin.storage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.sleuth.zipkin.stream.EnableZipkinStreamServer;

/**
 *  通过MQ 和服务之间进行消息传递，降低了耦合性
 *
 *   测试： 依次启动zipkin-storage-server、zipkin-storage-service-first、zipkin-storage-service-second
 *
 *   然后访问：
 *         http://localhost:8988/second
 *         http://localhost:8989/first
 *   观察结果：
 *         http://localhost:9411
 *   观看依赖分析和查找调用链
 *
 * @see https://blog.csdn.net/z8414/article/details/78600646
 */
@EnableZipkinStreamServer
@SpringBootApplication
public class ZipkinStorageApp {
    public static void main(String[] args) {
        SpringApplication.run(ZipkinStorageApp.class,args);
    }
}
