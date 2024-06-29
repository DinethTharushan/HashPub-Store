package com.jmsmessagetester.jmsmessagetester.util;

import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageTransfomer {
    public String parseStartNodeMessage (HPMessage hpMessage) {
        StringBuilder stringBuilder = new StringBuilder();

        stringBuilder.append(MessageTags.TAG_1_EVENT_TYPE);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getEventType());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_2_SENDER_PORT);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getSenderPort());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_3_RECEIVER_PORT);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getReceiverPort());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_4_STATUS);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getStatus());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_5_NODE_TYPE);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getNodeType());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_6_HASH_VAL);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getHashVal());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_7_VAL);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getVal());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        stringBuilder.append(MessageTags.TAG_8_DATA_RETREIVAL_REQ_ID);
        stringBuilder.append(MessageTags.TAG_EQUAL);
        stringBuilder.append(hpMessage.getDataRetreivalReqId());
        stringBuilder.append(MessageTags.TAG_SEPERATOR);

        String message = stringBuilder.toString();
        return message;
    }
}
