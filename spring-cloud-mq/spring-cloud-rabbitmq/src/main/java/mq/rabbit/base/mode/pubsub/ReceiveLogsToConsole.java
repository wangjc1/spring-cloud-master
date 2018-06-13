package mq.rabbit.base.mode.pubsub;

import com.rabbitmq.client.*;

import java.io.IOException;

public class ReceiveLogsToConsole {  
    private final static String EXCHANGE_NAME = "ex_log";    
    public static void main(String[] args) throws Exception {  
        // 创建连接和频道    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // 指定用户 密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 指定端口  
        factory.setPort(AMQP.PROTOCOL.PORT);  
        Connection connection = factory.newConnection();    
        Channel channel = connection.createChannel();    
    
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        // 创建一个非持久的、唯一的且自动删除的队列且队列名称由服务器随机产生一般情况这个名称与amq.gen-JzTY20BRgKO-HjmUJj0wLg 类似。
        // queueDeclare()方法有多个实现，可以定义是否持久化或队列名称
        String queueName = channel.queueDeclare().getQueue();    
        // 为转发器指定队列，设置binding    
        channel.queueBind(queueName, EXCHANGE_NAME, "");    
    
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");    
    
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        // 指定接收者，第二个参数为自动应答，无需手动应答
        channel.basicConsume(queueName, true, consumer);
    }  
  
}  