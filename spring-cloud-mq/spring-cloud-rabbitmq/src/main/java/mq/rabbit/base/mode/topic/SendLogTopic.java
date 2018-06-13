package mq.rabbit.base.mode.topic;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.util.UUID;

/**
 * 主题（Topic）模式：
 * 发送日志消息SendLogTopic，发送4个消息绑定不同的绑定键， "kernal.info", "cron.warning",  "auth.info", "kernel.critical"
 * <p/>
 * 测试：
 * 启动2个消费者，再启动发送4类消息生产者。观察接收到的消息，都收到对应的消息。可以看出使用topic类型的转发器，成功实现了多重条件选择的订阅。
 */
public class SendLogTopic {
    private static final String EXCHANGE_NAME = "topic_logs";

    public static void main(String[] args) throws Exception {
        // 创建连接和频道    
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        // 指定用户 密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        // 声明转发器  
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        //定义绑定键     
        String[] routing_keys = new String[]{"kernal.info", "cron.warning",
                "auth.info", "kernel.critical"};
        for (String routing_key : routing_keys) {
            //发送4条不同绑定键的消息  
            String msg = UUID.randomUUID().toString();
            channel.basicPublish(EXCHANGE_NAME, routing_key, null, msg
                    .getBytes());
            System.out.println(" [x] Sent routingKey = " + routing_key + " ,msg = " + msg + ".");
        }

        channel.close();
        connection.close();
    }

}  