package mq.rabbit.base.mode.point;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 点对点模式，根据队列名称精确匹配
 */
public class Receive {  
      
    //消息队列名称  
    private final static String QUEUE_NAME = "hello1";
  
    public static void main(String[] args) throws Exception {  
         //打开连接和创建频道，与发送端一样    
        ConnectionFactory factory = new ConnectionFactory();    
        factory.setHost("localhost");
        //指定用户 密码  
        factory.setUsername("guest");
        factory.setPassword("guest");
        //指定端口  
        factory.setPort(AMQP.PROTOCOL.PORT);  
        //创建一个连接    
        Connection connection = factory.newConnection();    
        //创建一个频道    
        Channel channel = connection.createChannel();    
        //声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。    
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //创建队列消费者
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }
  
}  