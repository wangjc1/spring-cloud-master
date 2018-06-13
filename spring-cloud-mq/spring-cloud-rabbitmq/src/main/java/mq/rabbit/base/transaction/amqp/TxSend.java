package mq.rabbit.base.transaction.amqp;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * 基于AMQP 事务模式
 *
 * 缺点： 只有消息成功被broker接受，事务提交才能成功，因为是同步发送，所以使用事务机制的话会降低RabbitMQ的性能
 */
public class TxSend {

    private static final String QUEUE_NAME = "test_queue_tx";

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

        try{
            channel.txSelect(); // 开启事务

            for(int i=0;i<30;i++){
                // 发送消息
                String message = "hello, tx message";
                channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
                System.out.println(" ["+i+"] Sent message : '" + message + "'");

                //模拟异常
                if(i==10){
                    int j = 1/0;
                }
            }

            channel.txCommit(); // 提交事务
        }catch (Exception e){
            channel.txRollback(); // 回滚事务
            System.out.println("send message txRollback");
        }

        channel.close();
        connection.close();
    }

}