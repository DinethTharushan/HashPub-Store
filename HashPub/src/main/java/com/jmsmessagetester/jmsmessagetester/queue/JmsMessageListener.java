package com.jmsmessagetester.jmsmessagetester.queue;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class JmsMessageListener {
    private JmsTemplate jmsTemplate;

    @Autowired
    public JmsMessageListener(JmsTemplate jmsTemplate) {
        this.jmsTemplate = jmsTemplate;
    }

    public void receiveMessage() {
        String receivedMessage  = (String) jmsTemplate.receiveAndConvert("java:/queue/INQUIRY");
        System.out.println("Received Queue Message : " + receivedMessage);
    }
}
