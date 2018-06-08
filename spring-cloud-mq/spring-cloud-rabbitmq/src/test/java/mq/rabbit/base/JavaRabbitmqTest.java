package mq.rabbit.base;

import java.util.HashMap;


public class JavaRabbitmqTest {
    public JavaRabbitmqTest() throws Exception {

        Consumer consumer = new Consumer("queue");
        Thread consumerThread = new Thread(consumer);
        consumerThread.start();

        Producer producer = new Producer("queue");

        for (int i = 0; i < 1000000; i++) {
            HashMap message = new HashMap();
            message.put("message number", i);
            producer.sendMessage(message);
            System.out.println("Message Number " + i + " sent.");
        }
    }

    public static void main(String[] args) throws Exception {
        new JavaRabbitmqTest();
    }
}  