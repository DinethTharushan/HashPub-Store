package com.jmsmessagetester.jmsmessagetester.logic;

import com.jmsmessagetester.jmsmessagetester.queue.JmsMessageSender;
import com.jmsmessagetester.jmsmessagetester.topic.JmsTopicMessageSender;
import com.jmsmessagetester.jmsmessagetester.util.HPConst;
import com.jmsmessagetester.jmsmessagetester.util.MessageTags;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import org.apache.catalina.startup.UserConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

@Component
public class DataRouter {

    @Autowired
    private NodeInitiater nodeInitiater;
    private final Map<String, String> valueMap = new HashMap<>();
    private boolean isGetUserInput = true; // get user input only after one input processes
    @Autowired
    private JmsMessageSender jmsMessageSender;
    @Autowired
    private JmsTopicMessageSender jmsTopicMessageSender;

    public void getUserInput () {
        Scanner scanner = new Scanner(System.in);
        if (nodeInitiater.getNodeMap().get(nodeInitiater.getPort()) == HPConst.NODE_TYPE_HASHER) {
            System.out.println("++++++++++++++Enter 1 : to enter new data / 2 : to retreive data +++++++++++++++");
            if (isGetUserInput == true) {
                setGetUserInput(false);
                int userInput = scanner.nextInt();
                scanner.nextLine();
                if (userInput == 1) {
                    System.out.println("Enter Data : ");
                    String newValue = scanner.nextLine();
                    processAndStoreData(newValue);
                } else if (userInput == 2) {
                    System.out.println("Enter hash value : ");
                    String hashValue = scanner.nextLine();
                    retreiveData(hashValue);
                }
                setGetUserInput(true);
            }
        } else if (nodeInitiater.getNodeMap().get(nodeInitiater.getPort()) == HPConst.NODE_TYPE_RECEIVER) {
            System.out.println("Enter hash value : ");
            if (isGetUserInput == true) {
                setGetUserInput(false);
                String hashValue = scanner.nextLine();
                retreiveData(hashValue);
                setGetUserInput(true);
            }
        }
    }

    public void processAndStoreData (String newValue) {
       String hashValue = newValue.substring(0, Math.min(newValue.length(), 10));
       String value = newValue;
       getCharVal(hashValue);
       // getting 2 receiver ids to store the value
       int[] responsibleReceivers = getResposibleReceiverIds(hashValue);
       // send those values to responsible receivers
       sendDataToResponsibleReceivers(responsibleReceivers, hashValue, value);
       System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++");
       System.out.println("Hash val : " + hashValue + " value : " + value + " primary Node : " + responsibleReceivers[0] + " secondary node : " + responsibleReceivers[1]);
       getUserInput();
    }

    public int[] getResposibleReceiverIds (String hashVal) {
        int charVal = getCharVal(hashVal);
        int receiverCount = getReceiverCount();
        ArrayList<Integer> receiverNodes = populateReceiverNodes();
        int primaryReceiver = receiverNodes.get(charVal%receiverCount);
        int secondaryReceiver = receiverNodes.get((charVal+1)%receiverCount);
        int[] responsibleNodes = new int[]{primaryReceiver, secondaryReceiver};
        return responsibleNodes;
    }

    public int getCharVal(String hashVal) {
        int sumOfString = 0;
        for (char ch : hashVal.toCharArray()) {
            sumOfString = sumOfString + (int) ch;
        }
        return sumOfString;
    }

    public int getReceiverCount() {
        int receiverCount = 0;
        for (Integer key : nodeInitiater.getNodeMap().keySet()) {
            if (nodeInitiater.getNodeMap().get(key) == HPConst.NODE_TYPE_RECEIVER) {
                receiverCount ++;
            }
        }
        return receiverCount;
    }

    public ArrayList<Integer> populateReceiverNodes() {
        ArrayList<Integer> receiverNodes = new ArrayList<>();
        for (Integer key : nodeInitiater.getNodeMap().keySet()) {
            if (nodeInitiater.getNodeMap().get(key) == HPConst.NODE_TYPE_RECEIVER) {
                receiverNodes.add(key);
            }
        }
        return receiverNodes;
    }

    public void sendDataToResponsibleReceivers(int[] receiverIds, String hashVal, String val) {
        HPMessage hpMessage = new HPMessage();
        hpMessage.setEventType(MessageTags.EVENT_TYPE_SEND_DATA_TO_RECEIVER);
        hpMessage.setSenderPort(nodeInitiater.getPort());
        hpMessage.setReceiverPort(receiverIds[0]);
        hpMessage.setHashVal(hashVal);
        hpMessage.setVal(val);
        // sent values to primary receiver
        jmsMessageSender.sendHPMessage(hpMessage);
        // sent value to secondary receive
        hpMessage.setReceiverPort(receiverIds[1]);
        jmsMessageSender.sendHPMessage(hpMessage);
    }

    public void retreiveData (String hashValue) {
        HPMessage hpMessage = new HPMessage();
        hpMessage.setEventType(MessageTags.EVENT_TYPE_RETREIVE_DATA);
        hpMessage.setSenderPort(nodeInitiater.getPort());
        hpMessage.setHashVal(hashValue);

        //generate unique int
        long currentTimeMillis = System.currentTimeMillis();
        int uniqueInteger = (int) (currentTimeMillis % 100000000);
        hpMessage.setDataRetreivalReqId(uniqueInteger);

        jmsTopicMessageSender.sendHPMessage(hpMessage);
        getUserInput();
    }

    public boolean isGetUserInput() {
        return isGetUserInput;
    }

    public void setGetUserInput(boolean getUserInput) {
        isGetUserInput = getUserInput;
    }
}
