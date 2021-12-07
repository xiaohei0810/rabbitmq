package com.xiaohei.rabbitmqdemo.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author liangyusheng@xiaomi.com
 * @Date 2021/12/7 4:11 下午
 * @Version 1.0
 * @Describtion 生产者
 */
public class RabbitProducer {

    private static final String EXCHANGE_NAME = "exchange_demo";
    private static final String ROUTING_KYE = "routingkye_demo";
    private static final String QUEUE_NAME = "queue_name";
    private static final String IP_ADDRESS = "localhost";
    private static final  int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");

        Connection connection = connectionFactory.newConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KYE);
        String message = "Hello world";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KYE, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

        channel.close();;
        connection.close();
    }

}
