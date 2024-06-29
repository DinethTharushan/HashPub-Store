package com.jmsmessagetester.jmsmessagetester.queue;

import com.jmsmessagetester.jmsmessagetester.logic.DataStore;
import com.jmsmessagetester.jmsmessagetester.logic.NodeInitiater;
import com.jmsmessagetester.jmsmessagetester.util.MessageParser;
import com.jmsmessagetester.jmsmessagetester.util.MessageTags;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import jakarta.jms.TextMessage;
import jakarta.jms.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class JmsQueueMDB {

    @Autowired
    private MessageParser messageParser;
    @Autowired
    private NodeInitiater nodeInitiater;
    @Value("${server.port}")
    private int receiverPort;
    @Autowired
    private DataStore dataStore;

    @JmsListener(destination = "queue${server.port}")
    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String messageText = ((TextMessage) message).getText();
                System.out.println("================================================");
                System.out.println("JmsqueueMDB received a message : " + messageText);
                HPMessage hpMessage = messageParser.parseMessage(messageText);
                switch (hpMessage.getEventType()) {
                    case MessageTags.EVENT_TYPE_NEW_NODE :
                        nodeInitiater.processReplyForNodeInitialization(hpMessage);
                        break;
                    case MessageTags.EVENT_TYPE_AKC_FOR_NODE_TYPE_UPDATE :
                        nodeInitiater.finalizeNodeType(hpMessage);
                        break;
                    case MessageTags.EVENT_TYPE_SEND_DATA_TO_RECEIVER:
                        dataStore.storeData(hpMessage);
                        break;
                    case MessageTags.EVENT_TYPE_ACK_FOR_DATA_STORE_REQUEST:
                        dataStore.processAckForDataStore(hpMessage);
                        break;
                    case MessageTags.EVENT_TYPE_ACK_FOR_RETREIVE_DATA:
                        dataStore.recordDataRetreivalResponse(hpMessage);
                        break;
                    default:
                        System.out.println("Invalid event type received by queue message listener.");
                        break;
                }
            } catch (Exception e) {
                System.out.println("JMS queue message invalid" + e);
            }
        } else {
            // handle non text messages
        }
    }
}
