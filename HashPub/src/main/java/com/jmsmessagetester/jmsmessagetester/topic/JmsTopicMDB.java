package com.jmsmessagetester.jmsmessagetester.topic;

import com.jmsmessagetester.jmsmessagetester.logic.DataStore;
import com.jmsmessagetester.jmsmessagetester.logic.NodeInitiater;
import com.jmsmessagetester.jmsmessagetester.util.MessageParser;
import com.jmsmessagetester.jmsmessagetester.util.MessageTags;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Component
public class JmsTopicMDB {

    @Autowired
    private NodeInitiater nodeInitiater;
    @Autowired
    private MessageParser messageParser;
    @Autowired
    private DataStore dataStore;

    @JmsListener(destination = "nodeStatusUpdate", containerFactory = "myJmsListenerContainerFactory")
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String messageText = ((TextMessage) message).getText();
                System.out.println("===================================================");
                System.out.println("JmsTopicMDB received a message : " + messageText);
                HPMessage hpMessage = messageParser.parseMessage(messageText);
                switch (hpMessage.getEventType()) {
                    case MessageTags.EVENT_TYPE_NEW_NODE :
                        nodeInitiater.processNodeStatusUpdate(hpMessage);
                        break;
                    case MessageTags.EVENT_TYPE_UPDATE_NODE_TYPE :
                        nodeInitiater.updateNodeMap(hpMessage);
                        break;
                    case MessageTags.EVENT_TYPE_RETREIVE_DATA:
                        dataStore.retreiveData(hpMessage);
                        break;
                    default:
                        System.out.println("Invalid event type received by topic listener");
                        break;
                }
            } catch (Exception e) {
                System.out.println("JMS topic message invalid" + e);
            }
        } else {
            // Handle non-text messages
        }
    }

}
