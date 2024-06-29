package com.jmsmessagetester.jmsmessagetester.logic;

import com.jmsmessagetester.jmsmessagetester.queue.JmsMessageSender;
import com.jmsmessagetester.jmsmessagetester.util.MessageTags;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class DataStore {

    private final Map<String, String> dataMap = new HashMap<>();
    @Autowired
    private NodeInitiater nodeInitiater;
    @Autowired
    private JmsMessageSender jmsMessageSender;
    private boolean isDisplayRetreivedData = false;
    private Map<String, String> retreivedDataMap = new HashMap<>();



    public void storeData (HPMessage hpMessage) {
        if (hpMessage.getReceiverPort() == nodeInitiater.getPort()) {
            dataMap.put(hpMessage.getHashVal(), hpMessage.getVal());
        }
        // send acknowledgement
        hpMessage.setEventType(MessageTags.EVENT_TYPE_ACK_FOR_DATA_STORE_REQUEST);
        hpMessage.setReceiverPort(hpMessage.getSenderPort());
        hpMessage.setSenderPort(nodeInitiater.getPort());
        jmsMessageSender.sendHPMessage(hpMessage);
        printDataMap();
    }

    public void processAckForDataStore (HPMessage hpMessage) {
        System.out.println("Successfully stored data in : " + hpMessage.getSenderPort());
        nodeInitiater.setGetUserInput(true);
    }

    public void retreiveData (HPMessage hpMessage) {
        if (dataMap.containsKey(hpMessage.getHashVal())) {
            HPMessage hpMessage1 = new HPMessage();
            hpMessage1.setEventType(MessageTags.EVENT_TYPE_ACK_FOR_RETREIVE_DATA);
            hpMessage1.setReceiverPort(hpMessage.getSenderPort());
            hpMessage1.setSenderPort(nodeInitiater.getPort());
            hpMessage1.setHashVal(hpMessage.getHashVal());
            hpMessage1.setVal(dataMap.get(hpMessage.getHashVal()));
            hpMessage1.setDataRetreivalReqId(hpMessage.getDataRetreivalReqId());
            jmsMessageSender.sendHPMessage(hpMessage1);
        }
        nodeInitiater.setGetUserInput(true);
    }

    public void recordDataRetreivalResponse (HPMessage hpMessage) {
        retreivedDataMap.put(hpMessage.getHashVal(), hpMessage.getVal());
        setDisplayRetreivedData(true);
    }

    public void displayRetreivedData () {
        System.out.println("++++++++++ Print Retreived Data +++++++++");
       for (String key:retreivedDataMap.keySet()){
           System.out.println("Retreived Data for Hash value : " + key + " value : " + retreivedDataMap.get(key));
           retreivedDataMap.remove(key);
       }
       nodeInitiater.setGetUserInput(true);
    }

    public void printDataMap () {
        System.out.println("++++++++++++ Data Map +++++++++++++");
        for (String key : dataMap.keySet()) {
            System.out.println("hash : " + key + " value : " + dataMap.get(key));
        }
        nodeInitiater.setGetUserInput(true);
    }

    public Map<String, String> getDataMap() {
        return dataMap;
    }

    public Map<String, String> getRetreivedDataMap() {
        return retreivedDataMap;
    }

    public void setRetreivedDataMap(Map<String, String> retreivedDataMap) {
        this.retreivedDataMap = retreivedDataMap;
    }

    public boolean isDisplayRetreivedData() {
        return isDisplayRetreivedData;
    }

    public void setDisplayRetreivedData(boolean displayRetreivedData) {
        isDisplayRetreivedData = displayRetreivedData;
    }
}
