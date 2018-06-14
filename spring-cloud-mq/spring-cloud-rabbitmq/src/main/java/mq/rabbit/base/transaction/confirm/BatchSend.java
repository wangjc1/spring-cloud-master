package mq.rabbit.base.transaction.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * 基于批量确认的 事务模式
 *
 */
public class BatchSend {

    private static final String QUEUE_NAME = "test_queue_batch_confirm";

    public static void main(String[] args) throws Exception {
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //指定用户 密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        //指定端口
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();

        // 从连接开一个通道
        Channel channel = connection.createChannel();
        // 声明一个队列
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);

        //开启批量确认模式
        channel.confirmSelect();

        //监听消息确认消息，批量模式下，每当发送一批消息成功后，broker发送一个确认消息
        channel.addConfirmListener(new ConfirmListener() {
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("nack: deliveryTag = " + deliveryTag + " multiple: " + multiple);
            }

            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("ack: deliveryTag = " + deliveryTag + " multiple: " + multiple);
            }
        });

        try{
            for(int i=0;i<30;i++){
                // 发送消息
                String message = "hello, batch message["+i+"]";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" ["+i+"] Sent message : '" + message + "'");
            }
        }catch (Exception e){
            System.out.println("send message failed");
        }


        //等待回复，如果回复true
        try{
            //channel.waitForConfirmsOrDie();
            if (channel.waitForConfirms()) {
                System.out.println("发送成功");
            }
            else {
                System.out.println("发送失败");
            }
        }finally {
            TimeUnit.MILLISECONDS.sleep(30);
            channel.close();
            connection.close();
        }
    }

}