package mq.rabbit.base.mode.route;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * 路由模式
 * 注：发送消息时可以设置routing_key，接收队列与转发器间可以设置binding_key，接收者接收与binding_key与routing_key相同的消息。
 *
 * 测试mandatory参数：
 *  只启动生产端，不启动消费端，当生产端发送消息时找不到一个合适的队列，这时调用channel.addReturnListener
 *  测试的时候注意ReturnListener在另一个线程中，所以打印的消息可能不显示，所以让主线程休眠一会看日志消息
 */
public class SendLogDirect {
      
     //交换名称  
     private static final String EXCHANGE_NAME = "ex_logs_direct";    
     //日志分类  
     private static final String[] SEVERITIES = { "info", "warning", "error"};
          
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

        //添加回调监听，用于处理basic.return命令返回的消息
        channel.addReturnListener(new ReturnListener() {
            public void handleReturn(int replyCode, String replyText, String exchange, String routingKey, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                System.err.println(Thread.currentThread().getName()+" [E] The route key '"+routingKey+"' is not exist. '" + new String(body) + "'");
            }
        });

        // 当mandatory标志位设置为true时，如果exchange根据自身类型和消息routeKey无法找到一个符合条件的queue，
        // 那么会调用basic.return方法将消息返还给生产者, channel.addReturnListener添加一个监听器，
        // 当broker执行basic.return方法时，会回调handleReturn方法，这样我们就可以处理变为死信的消息了；
        // 当mandatory设为false时，出现上述情形broker会直接将消息扔掉;通俗的讲，mandatory标志告诉broker
        // 代理服务器至少将消息route到一个队列中，否则就将消息return给发送者。
        boolean mandatory=true;
    
        //发送6条消息    
        for (int i = 0; i < 6; i++)    
        {    
            String severity = getSeverity();    
            String message = severity + "_log :" + UUID.randomUUID().toString();    
            // 发布消息至转发器，指定routingkey    
            channel.basicPublish(EXCHANGE_NAME, severity, mandatory, null, message.getBytes());
            System.out.println(Thread.currentThread().getName()+" [x] Sent '" + message + "'");
        }

        //休眠一会，查看其它线程日志信息(测试mandatory参数)
        TimeUnit.MILLISECONDS.sleep(20);

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
        return SEVERITIES[ranVal%SEVERITIES.length];
    }    
  
}  