https://gitee.com/jempson/rocketmq-spring-boot-starter/tree/master

引入依赖
<dependency>
    <groupId>.me.jollyfly</groupId>
    <artifactId>rocketmq-spring-boot-starter</artifactId>
    <version>1.3.1.RELEASE</version>
</dependency>
在配置类上添加@EnableRocket
@SpringBootApplication
@EnableRocket
public class MyApp {

    public static void main(String[] args) {
        SpringApplication.run(MyApp.class,args);
    }
}
application.properties
rocketmq.name-srv-addr=localhost:9876
创建监听
@RocketListeners(topic = "MY_TOPIC")
public class MyListener {

    @RocketMQListener(messageClass = String.class,tag = "TAG_1")
    public void method1(String message){
        System.out.println(message);
    }

    @RocketMQListener(messageClass = Object.class,tag = "TAG_2")
    public void method2(Object message){
        System.out.println(message.toString());
    }

}
使用说明 添加有@RocketListeners注解的类会自动转化为一个Consumer,类中不同的方法，通过RocketMQListener 注解，配置不同的tag消费不同tag的消息。
核心组件 RocketMessageListenerContainer 该组件是一个Consumer容器，容器实现了Spring的SmartLifecycle接口，容器的生命周期由Spring容器进行智能控制 系统中的所有Consumer的生命周期由该容器进行管理。容器可以对Consumer的消费行为进行控制和管理，同时提供Consumer 各个运行信息的获取接口。