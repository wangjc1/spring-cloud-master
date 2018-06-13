package mq.rabbit.base.mode.route;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.Random;
import java.util.UUID;

/**
 * 路由模式
 * 注：发送消息时可以设置routing_key，接收队列与转发器间可以设置binding_key，接收者接收与binding_key与routing_key相同的消息。
 */
//随机发送6条随机类型（routing key）的日志给转发器~~  
public class SendLogDirect {  
      
     //交换名称  
     private static final String EXCHANGE_NAME = "ex_logs_direct";    
     //日志分类  
     private static final String[] SEVERITIES = { "info", "warning", "error" };    
          
    public static void main(String[] args) throws Exception {  
        //创建连接和频道    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // 指定用户 密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        // 指定端口  
        factory.setPort(AMQP.PROTOCOL.PORT);  
        Connection connection = factory.newConnection();    
        Channel channel = connection.createChannel();    
        // 声明转发器的类型    
        channel.exchangeDeclare(EXCHANGE_NAME, "direct");    
    
        //发送6条消息    
        for (int i = 0; i < 6; i++)    
        {    
            String severity = getSeverity();    
            String message = severity + "_log :" + UUID.randomUUID().toString();    
            // 发布消息至转发器，指定routingkey    
            channel.basicPublish(EXCHANGE_NAME, severity, null, message  .getBytes());    
            System.out.println(" [x] Sent '" + message + "'");    
        }    
    
        channel.close();    
        connection.close();    
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