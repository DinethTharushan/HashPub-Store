package com.jmsmessagetester.jmsmessagetester.queue;
import com.jmsmessagetester.jmsmessagetester.util.MessageTransfomer;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageSender {

    private final JmsTemplate jmsTemplate;
    @Autowired
    private MessageTransfomer messageTransfomer = new MessageTransfomer();

    @Autowired
    public JmsMessageSender(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void sendHPMessage (HPMessage hpMessage) {
        try {
            String messageStr = messageTransfomer.parseStartNodeMessage(hpMessage);
            System.out.println("Sent node initialization reply message : " + messageStr);
            sendMessage("queue" + hpMessage.getReceiverPort(), messageStr);
        } catch (Exception e) {
            System.out.println("Message parsing error : " + e);
        }
    }

    public void sendMessage(String queueName, String message) {
        try {
            jmsTemplate.convertAndSend(queueName, message);
        } catch (Exception e) {
            System.out.println("Queue message sending error : " + e);
        }
    }
}







