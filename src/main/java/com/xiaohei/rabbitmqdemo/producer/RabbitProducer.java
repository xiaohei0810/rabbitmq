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
    // rabbitmq服务默认端口号为5672
    private static final  int PORT = 5672;

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(IP_ADDRESS);
        connectionFactory.setPort(PORT);
        connectionFactory.setUsername("root");
        connectionFactory.setPassword("root");

        // 创建连接
        Connection connection = connectionFactory.newConnection();
        // 创建信道
        Channel channel = connection.createChannel();
        // 创建一个type="direc"、持久化的、非自动删除的交换器
        channel.exchangeDeclare(EXCHANGE_NAME,"direct",true,false,null);
        // 创建一个持久化的、非排他的、非自动删除的队列
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        // 将交换器和队列通过路由健绑定
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,ROUTING_KYE);
        // 发送一条只久化消息
        String message = "Hello world，I am 小黑";
        channel.basicPublish(EXCHANGE_NAME,ROUTING_KYE, MessageProperties.PERSISTENT_TEXT_PLAIN,message.getBytes());

        // 关闭资源
        channel.close();;
        connection.close();
    }

}
