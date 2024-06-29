package com.jmsmessagetester.jmsmessagetester.logic;

import com.jmsmessagetester.jmsmessagetester.ActiveMQConfig;
import com.jmsmessagetester.jmsmessagetester.queue.JmsMessageListener;
import com.jmsmessagetester.jmsmessagetester.queue.JmsMessageSender;
import com.jmsmessagetester.jmsmessagetester.topic.JmsTopicMessageSender;
import com.jmsmessagetester.jmsmessagetester.util.MessageTransfomer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@Component
public class MessageRouter {
    private final Environment environment;
    private int port;
    @Autowired
    private MessageTransfomer messageTransfomer;


    public MessageRouter(Environment environment) {
            this.environment = environment;
        }

    public void testQueueMessages () {
        port  = Integer.parseInt(environment.getProperty("server.port"));
        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        JmsTemplate jmsjmsTemplate = activeMQConfig.jmsTemplate();
        JmsMessageSender jmsMessageSender = new JmsMessageSender(jmsjmsTemplate);
        JmsMessageListener jmsMessageListener = new JmsMessageListener(jmsjmsTemplate);
        if (port == 8081) {
            jmsMessageSender.sendMessage("queue","My name is Dineth");
        } else if (port == 8082) {
            jmsMessageListener.receiveMessage();
        }
    }

    public void testTopicMessages () {
        port = Integer.parseInt(environment.getProperty("server.port"));
        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        JmsTemplate jmsTopicTemplate = activeMQConfig.jmsTopicTemplate();
        JmsTopicMessageSender jmsTopicMessageSender = new JmsTopicMessageSender(jmsTopicTemplate);
        if (port == 8083) {
            jmsTopicMessageSender.sendToNodeStatusUpdateTopic("My name is Tharushan");
        }
    }

    public void startNode () {
        NodeInitiater nodeInitiater = new NodeInitiater(environment);
        nodeInitiater.initiateNewNode();
    }
}
