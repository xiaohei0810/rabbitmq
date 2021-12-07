package com.xiaohei.rabbitmqdemo.consumer;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Address;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * @Author liangyusheng@xiaomi.com
 * @Date 2021/12/7 4:25 下午
 * @Version 1.0
 * @Describtion 消费者
 */
public class RabbitConsumer {

    private static final String QUEUE_NAME = "queue_name";
    private static final String IP_ADDRESS = "localhost";
    private static final  int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

       Address[] addresses =  new Address[]{
                new Address(IP_ADDRESS,PORT)
        };

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");

        // 创建连接
        Connection connection = connectionFactory.newConnection(addresses);
        // 创建信道
        Channel channel = connection.createChannel();
        // 设置客户端最多接受未被ack的消息的个数
        channel.basicQos(64);
        Consumer consumer = new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties,byte[] body) throws IOException {
                System.out.println("recv message:" + new String(body));
                try {
                    TimeUnit.SECONDS.sleep(1);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                System.out.println(envelope.getDeliveryTag());
                channel.basicAck(envelope.getDeliveryTag(),false);
            }
        };
        // 消费
        channel.basicConsume(QUEUE_NAME,consumer);
        TimeUnit.SECONDS.sleep(5);
        channel.close();
        connection.close();
    }

}
