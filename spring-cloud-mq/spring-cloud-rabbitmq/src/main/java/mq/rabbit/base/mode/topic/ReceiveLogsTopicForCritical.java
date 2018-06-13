package mq.rabbit.base.mode.topic;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 接收*.critical消息
 */
public class ReceiveLogsTopicForCritical {

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
        // 随机生成一个队列    
        String queueName = channel.queueDeclare().getQueue();
        // 接收所有与kernel相关的消息    
        channel.queueBind(queueName, EXCHANGE_NAME, "*.critical");

        System.out
                .println(" [*] Waiting for critical messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body);
                String routingKey = envelope.getRoutingKey();
                System.out.println(" [x] Received routingKey = " + routingKey
                        + ",msg = " + message + ".");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }

}  