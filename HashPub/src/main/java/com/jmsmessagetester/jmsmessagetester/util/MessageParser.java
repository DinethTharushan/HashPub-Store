package com.jmsmessagetester.jmsmessagetester.util;

import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import jakarta.jms.Message;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Component
public class MessageParser {

    public HPMessage parseMessage(String message) {
        HPMessage hpMessage = new HPMessage();

        // split the message to tags
        String[] messageBlocks = message.split("\\|");
        List<String> preDefinedTags = Arrays.asList(MessageTags.TAG_1_EVENT_TYPE, MessageTags.TAG_2_SENDER_PORT,
                MessageTags.TAG_3_RECEIVER_PORT, MessageTags.TAG_4_STATUS, MessageTags.TAG_5_NODE_TYPE, MessageTags.TAG_6_HASH_VAL,
                MessageTags.TAG_7_VAL, MessageTags.TAG_8_DATA_RETREIVAL_REQ_ID);

        for (String tag : messageBlocks) {
            String[] parts = tag.split("=");
            if(parts.length == 2) {
                String key = parts[0];
                String value = parts[1];

                if (preDefinedTags.contains(key)) {
                    switch (key) {
                        case MessageTags.TAG_1_EVENT_TYPE:
                            hpMessage.setEventType(Integer.parseInt(value));
                            break;
                        case MessageTags.TAG_2_SENDER_PORT:
                            hpMessage.setSenderPort(Integer.parseInt(value));
                            break;
                        case MessageTags.TAG_3_RECEIVER_PORT:
                            hpMessage.setReceiverPort(Integer.parseInt(value));
                            break;
                        case MessageTags.TAG_4_STATUS:
                            hpMessage.setStatus(Integer.parseInt(value));
                            break;
                        case MessageTags.TAG_5_NODE_TYPE:
                            hpMessage.setNodeType(Integer.parseInt(value));
                            break;
                        case MessageTags.TAG_6_HASH_VAL:
                            hpMessage.setHashVal(value);
                            break;
                        case MessageTags.TAG_7_VAL:
                            hpMessage.setVal(value);
                            break;
                        case MessageTags.TAG_8_DATA_RETREIVAL_REQ_ID:
                            hpMessage.setDataRetreivalReqId(Integer.parseInt(value));
                            break;
                        default:
                            System.out.println("Invalid tag");
                            break;
                    }
                }
            }
        }
        return hpMessage;
    }
}
