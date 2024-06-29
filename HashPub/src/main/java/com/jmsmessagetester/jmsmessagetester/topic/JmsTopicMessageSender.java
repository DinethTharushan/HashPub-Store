package com.jmsmessagetester.jmsmessagetester.topic;

import com.jmsmessagetester.jmsmessagetester.ActiveMQConfig;
import com.jmsmessagetester.jmsmessagetester.util.MessageTags;
import com.jmsmessagetester.jmsmessagetester.util.MessageTransfomer;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsTopicMessageSender {

    private JmsTemplate jmsTopicTemplate;
    @Autowired
    private MessageTransfomer messageTransfomer;

    @Autowired
    public JmsTopicMessageSender(JmsTemplate jmsTopicTemplate) {
        this.jmsTopicTemplate = jmsTopicTemplate;
    }

    public void sendHPMessage(HPMessage hpMessage) {
        String message = messageTransfomer.parseStartNodeMessage(hpMessage);
        sendToNodeStatusUpdateTopic(message);
    }

    public void sendToNodeStatusUpdateTopic (String message) {
        try {
            System.out.println("Sent Topic Message : " + message);
            jmsTopicTemplate.convertAndSend("nodeStatusUpdate", message);
        } catch (Exception e) {
            System.out.println("Topic message sending error : " + e);
        }

    }
}
