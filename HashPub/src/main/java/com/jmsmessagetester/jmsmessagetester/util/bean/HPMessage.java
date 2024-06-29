package com.jmsmessagetester.jmsmessagetester.util.bean;

import org.springframework.stereotype.Component;

@Component
public class HPMessage {
    private int eventType = -1;
    private int senderPort = -1;
    private int receiverPort = -1;
    private int status = -1;
    private int nodeType = -1;
    private String hashVal = "";
    private String val = "";
    private int dataRetreivalReqId = -1;

    public int getEventType() {
        return eventType;
    }

    public void setEventType(int eventType) {
        this.eventType = eventType;
    }

    public int getSenderPort() {
        return senderPort;
    }

    public void setSenderPort(int senderPort) {
        this.senderPort = senderPort;
    }

    public int getReceiverPort() {
        return receiverPort;
    }

    public void setReceiverPort(int receiverPort) {
        this.receiverPort = receiverPort;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getNodeType() {
        return nodeType;
    }

    public void setNodeType(int nodeType) {
        this.nodeType = nodeType;
    }

    public String getHashVal() {
        return hashVal;
    }

    public void setHashVal(String hashVal) {
        this.hashVal = hashVal;
    }

    public String getVal() {
        return val;
    }

    public void setVal(String val) {
        this.val = val;
    }

    public int getDataRetreivalReqId() {
        return dataRetreivalReqId;
    }

    public void setDataRetreivalReqId(int dataRetreivalReqId) {
        this.dataRetreivalReqId = dataRetreivalReqId;
    }
}
