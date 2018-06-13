package mq.rabbit.base.mode.work;

import com.rabbitmq.client.*;

import java.io.IOException;

/**
 * 测试的时候，启动多个worker
 */
public class Worker {
    //队列名称
    private final static String QUEUE_NAME = "task_queue";

    public static void main(String[] args) throws Exception {
        //区分不同工作进程的输出
        int hashCode = Worker.class.hashCode();
        //创建连接和频道
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        //指定用户 密码
        factory.setUsername("guest");
        factory.setPassword("guest");
        //指定端口
        factory.setPort(AMQP.PROTOCOL.PORT);
        Connection connection = factory.newConnection();
        final Channel channel = connection.createChannel();
        boolean durable = false; //设置消息持久化  RabbitMQ不允许使用不同的参数重新定义一个队列，所以已经存在的队列，我们无法修改其属性。
        //声明队列
        channel.queueDeclare(QUEUE_NAME, durable, false, false, null);

        /**
         * ack= true: Round-robin 转发   消费者被杀死，消息会丢失
         * ack=false:消息应答 ，为了保证消息永远不会丢失，RabbitMQ支持消息应答（message acknowledgments）。
         * 消费者发送应答给RabbitMQ，告诉它信息已经被接收和处理，然后RabbitMQ可以自由的进行信息删除。
         * 如果消费者被杀死而没有发送应答，RabbitMQ会认为该信息没有被完全的处理，然后将会重新转发给别的消费者。
         * 通过这种方式，你可以确认信息不会被丢失，即使消者偶尔被杀死。
         * 消费者需要耗费特别特别长的时间是允许的。
         *
         */

        //公平转发  设置最大服务转发消息数量    只有在消费者空闲的时候会发送下一条信息。
        int prefetchCount = 1;
        channel.basicQos(prefetchCount);

        final Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");

                System.out.println(" [x] Received '" + message + "'");
                try {
                    doWork(message);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    System.out.println(" [x] Done");
                    //如果不是自动应答(ack=false)，消息被消费后没有发送应答消息，则服务器端会安排该消息重新进入队列，等待投递给下一个消费者
                    //RabbitMQ不会为未ack的消息设置超时时间，它判断此消息是否需要重新投递给消费者的唯一依据是消费该消息的消费者连接是否已经断开
                    //RabbitMQ管理平台界面上可以看到当前队列中Ready状态和Unacknowledged状态的消息数，分别对应上文中的等待投递给消费者的消息数和已经投递给消费者但是未收到ack信号的消息数
                    channel.basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };

        //true为打开应答机制
        boolean ack = false;
        // 指定消费队列
        channel.basicConsume(QUEUE_NAME, ack, consumer);
    }

    /**
     * 每个点耗时1s
     *
     * @param task
     * @throws InterruptedException
     */
    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.')
                Thread.sleep(1000);
        }
    }
}