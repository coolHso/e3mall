package cn.e3mall.search.activemq;
import cn.e3mall.search.service.ItemSearchService;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

public class ItemAddMessageListener implements MessageListener {

    @Autowired
    private ItemSearchService itemSearchService;

    @Override
    public void onMessage(Message message) {
        // System.out.println("receive message successful");
        Long itemId = null;
        TextMessage textMessage;
        try {
            if (message instanceof TextMessage) {
                textMessage = (TextMessage) message;
                itemId = Long.parseLong(textMessage.getText());
            }
            itemSearchService.addDocument(itemId);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
