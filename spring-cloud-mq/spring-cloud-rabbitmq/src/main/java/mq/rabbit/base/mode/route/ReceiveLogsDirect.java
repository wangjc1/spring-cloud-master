package mq.rabbit.base.mode.route;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
  
//接收端随机设置一个日志严重级别（binding_key）。。。  
public class ReceiveLogsDirect {  
      
    private static final String EXCHANGE_NAME = "ex_logs_direct";    
    private static final String[] SEVERITIES = { "info", "warning", "error" };    
  
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
        // 声明direct类型转发器    
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");    
    
        String queueName = channel.queueDeclare().getQueue();    
        String severity = getSeverity();    
        // 指定binding_key    
        channel.queueBind(queueName, EXCHANGE_NAME, severity);    
        System.out.println(" [*] Waiting for "+severity+" logs. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    
    }
    
    /**  
     * 随机产生一种日志类型  
     *   
     * @return  
     */    
    private static String getSeverity()    
    {    
        Random random = new Random();    
        int ranVal = random.nextInt(3);    
        return SEVERITIES[ranVal];    
    }    
}  