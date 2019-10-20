package cn;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.junit.Test;

import javax.jms.*;

public class ActiveMQTest {

    @Test
    public void QueueProducerTest() throws Exception{
        // 1、创建工厂对象,参数为服务端的ip及端口
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.131:61616");
        //使用工厂创建一个连接对象
        Connection connection = activeMQConnectionFactory.createConnection();
        //开启连接
        connection.start();
        //使用Connection对象创建Session，第一参数为是否开启事务，为true时第二参数不起作用
        // 第二参数为应答模式，1、自动应答；2、手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用session创建一个Destination对象(queue或topic)
        Queue queue = session.createQueue("test-queue");
        // 使用session对象创建一个producer对象
        MessageProducer producer = session.createProducer(queue);
        //使用session创建消息
        TextMessage textMessage = session.createTextMessage("this is a producer's test message2");
        //使用producer发送消息
        producer.send(textMessage);
        // 关闭资源
        producer.close();
        session.close();
        connection.close();
    }

    @Test
    public void queueConsumerTest() throws Exception{
        /*创建工厂*/
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.131:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Queue queue = session.createQueue("test-queue");
        MessageConsumer consumer = session.createConsumer(queue);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    String text = ((TextMessage) message).getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 等待
        System.in.read();
       // 关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

    @Test
    public void TopicProducerTest() throws Exception{
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.131:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        //使用Connection对象创建Session，第一参数为是否开启事务，为true时第二参数不起作用
        // 第二参数为应答模式，1、自动应答；2、手动应答
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        //使用session创建一个Destination对象(queue或topic)
        Topic topic = session.createTopic("test-topic");
        // 使用session对象创建一个producer对象
        MessageProducer producer = session.createProducer(topic);
        //使用session创建消息
        TextMessage textMessage = session.createTextMessage("topic message");
        //使用producer发送消息
        producer.send(textMessage);
        // 关闭资源
        producer.close();
        session.close();
        connection.close();
    }


    @Test
    public void topicConsumerTest() throws Exception{
        ActiveMQConnectionFactory activeMQConnectionFactory = new ActiveMQConnectionFactory("tcp://192.168.1.131:61616");
        Connection connection = activeMQConnectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic topic = session.createTopic("test-topic");
        MessageConsumer consumer = session.createConsumer(topic);
        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                try {
                    String text = ((TextMessage) message).getText();
                    System.out.println(text);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
        // 等待
        System.out.println("消费者2启动");
        System.in.read();
        // 关闭资源
        consumer.close();
        session.close();
        connection.close();
    }

}
