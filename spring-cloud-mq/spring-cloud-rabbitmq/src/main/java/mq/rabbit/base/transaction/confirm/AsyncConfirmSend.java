package mq.rabbit.base.transaction.confirm;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * 异步confirm模式 模式
 *
 * 这里模拟发送到第10条时休眠一会，这时停止消费端接受消息，让后面20条无法确认，然后重新启动消费端，查看后面20条是否能重新被投递后消费
 */
public class AsyncConfirmSend {

    private static final String QUEUE_NAME = "test_queue_async_confirm";

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

        //生产者调用confirmSelect() 将channel设为confirm模式
        channel.confirmSelect();

        //存放未确认的消息
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        //通道添加监听
        channel.addConfirmListener(new ConfirmListener() {
            /**
             * 处理返回确认成功
             * @param deliveryTag 如果是多条，这个就是最后一条消息的tag
             * @param multiple 是否多条
             * @throws IOException
             */
            @Override
            public void handleAck(long deliveryTag, boolean multiple) throws IOException {

                System.out.println("消息发送成功,deliveryTag:" + deliveryTag + "multiple:" + multiple + "");
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }

            /**
             * 处理返回确认失败，handleNack 3s 10s xxx.. 重试
             * @param deliveryTag
             * @param multiple
             * @throws IOException
             */
            @Override
            public void handleNack(long deliveryTag, boolean multiple) throws IOException {
                System.out.println("失败,deliveryTag:" + deliveryTag + "multiple:" + multiple + "");
                if (multiple) {
                    confirmSet.headSet(deliveryTag + 1).clear();
                } else {
                    confirmSet.remove(deliveryTag);
                }
            }
        });

        for(int i=0;i<30;i++){
            // 发送消息
            String message = "hello, async message";
            long tag = channel.getNextPublishSeqNo();
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
            System.out.println(String.format(" [%s,%s] Sent message : '" + message + "'",i,tag));
            //这里模拟发送到第10条时休眠一会，这时停止消费端接受消息，让后面20条无法确认
            if(i==10){
                TimeUnit.SECONDS.sleep(30);
            }
            confirmSet.add(tag);
        }

        channel.close();
        connection.close();

    }

}