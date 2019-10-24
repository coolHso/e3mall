package cn.activemq;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.TextMessage;

public class ActiveMQTest {

    @Test
    public void ReceiveMessageTest() throws Exception{
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring/applicationContext-activemq.xml");
        System.in.read();
        // JmsTemplate jmsTemplate = applicationContext.getBean(JmsTemplate.class);
        // Destination destination = (Destination) applicationContext.getBean("queueDestination");
        // Message receive = jmsTemplate.receive(destination);
        // TextMessage textMessage = (TextMessage) receive;
        // String text = textMessage.getText();
        // System.out.println(text);

    }



}
