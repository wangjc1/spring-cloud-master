package mq.rabbit.base.transaction.confirm;

import com.rabbitmq.client.*;

import java.util.concurrent.TimeUnit;

/**
 * 基于批量确认的 事务模式
 *
 * 这里模拟发送到第10条时休眠一会，这时停止消费端接受消息，让后面20条无法确认，然后重新启动消费端，查看后面20条是否能重新被投递后消费
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

        try{
            for(int i=0;i<30;i++){
                // 发送消息
                String message = "hello, batch message";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" ["+i+"] Sent message : '" + message + "'");

                //这里模拟发送到第10条时休眠一会，这时停止消费端接受消息，让后面20条无法确认
                if(i==10){
                    TimeUnit.SECONDS.sleep(30);
                }
            }
        }catch (Exception e){
            System.out.println("send message failed");
        }

        //等待回复，如果回复true
        try{
            if (channel.waitForConfirms()) {
                System.out.println("发送成功");
            }
            else {
                System.out.println("发送失败");
            }
        }finally {
            channel.close();
            connection.close();
        }
    }

}