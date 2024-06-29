package com.jmsmessagetester.jmsmessagetester.logic;

import com.jmsmessagetester.jmsmessagetester.ActiveMQConfig;
import com.jmsmessagetester.jmsmessagetester.queue.JmsMessageSender;
import com.jmsmessagetester.jmsmessagetester.topic.JmsTopicMessageSender;
import com.jmsmessagetester.jmsmessagetester.util.HPConst;
import com.jmsmessagetester.jmsmessagetester.util.MessageParser;
import com.jmsmessagetester.jmsmessagetester.util.MessageTags;
import com.jmsmessagetester.jmsmessagetester.util.MessageTransfomer;
import com.jmsmessagetester.jmsmessagetester.util.bean.HPMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class NodeInitiater {

    @Autowired
    private MessageParser messageParser = new MessageParser();
    @Autowired
    private MessageTransfomer messageTransfomer = new MessageTransfomer();
    @Autowired
    private JmsMessageSender jmsMessageSender;
    private final Map<Integer, Integer> nodeMap = new HashMap<>();
    private boolean isPrintNodeMap; // this is to trigger print node map trigger after finalize node type for newly created node
    private boolean isGetUserInput; // this is to get user input from the scheduler
    private Environment environment;
    private int port = -1;
    private AtomicInteger replyCountForNodeInitiation = new AtomicInteger(0);

    NodeInitiater(Environment environment) {
        this.environment = environment;
        this.port = Integer.parseInt(environment.getProperty("server.port"));
    }

    // to send an initial message to jms topic
    public void initiateNewNode () {
        HPMessage hpMessage = new HPMessage();
        hpMessage.setEventType(MessageTags.EVENT_TYPE_NEW_NODE);
        hpMessage.setStatus(MessageTags.NODE_STATUS_START);
        hpMessage.setSenderPort(Integer.parseInt(environment.getProperty("server.port")));
        String nodeStatusUpdatemessage = messageTransfomer.parseStartNodeMessage(hpMessage);

        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        JmsTemplate jmsTopicTemplate = activeMQConfig.jmsTopicTemplate();
        JmsTopicMessageSender jmsTopicMessageSender = new JmsTopicMessageSender(jmsTopicTemplate);
        jmsTopicMessageSender.sendToNodeStatusUpdateTopic(nodeStatusUpdatemessage);
    }

    // to process the initial message from jms topic
    public void processNodeStatusUpdate (HPMessage hpMessage) {
        if (hpMessage.getStatus() == MessageTags.NODE_STATUS_START && port != hpMessage.getSenderPort()) {
            replyNodeStatusUpdate(hpMessage);
        }
    }

    // to reply the initial message through jms queue
    public void replyNodeStatusUpdate(HPMessage nodeStatusUpdateMessage) {
        nodeStatusUpdateMessage.setStatus(MessageTags.STATUS_REPLY_MESSAGE);
        nodeStatusUpdateMessage.setReceiverPort(nodeStatusUpdateMessage.getSenderPort());
        nodeStatusUpdateMessage.setSenderPort(port);
        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        JmsTemplate jmsTemplate = activeMQConfig.jmsTemplate();
        JmsMessageSender jmsMessageSender = new JmsMessageSender(jmsTemplate);
        jmsMessageSender.sendHPMessage(nodeStatusUpdateMessage);
        System.out.println("New Node " + nodeStatusUpdateMessage.getReceiverPort() + " is up and send status update message.");
    }

    public void processReplyForNodeInitialization(HPMessage hpMessage) {
        if (port == hpMessage.getReceiverPort() && hpMessage.getStatus() == MessageTags.STATUS_REPLY_MESSAGE) {
            replyCountForNodeInitiation.incrementAndGet();
            System.out.println("Processing the reply message for node initialization. Reply count : " + replyCountForNodeInitiation.get());
        } else {
            System.out.println("Discarding the unwanted message.");
        }
    }

    // this method calls after 3 seconds up the node
    public void assignType () {
        int nodeType = -1; // 0=receiver, 1=hasher
        if (getReplyCountForNodeInitiation()%2 == 0) {
            nodeType = HPConst.NODE_TYPE_RECEIVER;
        } else {
            nodeType = HPConst.NODE_TYPE_HASHER;
        }
        HPMessage hpMessage = new HPMessage();
        hpMessage.setEventType(MessageTags.EVENT_TYPE_UPDATE_NODE_TYPE);
        hpMessage.setSenderPort(port);
        hpMessage.setNodeType(nodeType);
        nodeMap.put(port, nodeType);
        String message = messageTransfomer.parseStartNodeMessage(hpMessage);

        //send the type update message through topic
        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        JmsTemplate jmsTopicTemplate = activeMQConfig.jmsTopicTemplate();
        JmsTopicMessageSender jmsTopicMessageSender = new JmsTopicMessageSender(jmsTopicTemplate);
        jmsTopicMessageSender.sendToNodeStatusUpdateTopic(message);
        System.out.println("The node is running on port : " + port + " is a " + nodeType + ". Broadcast the node type update message.");
    }

    // update the node map according to the node type update queue message
    public void updateNodeMap (HPMessage hpMessage) {
        int key = hpMessage.getSenderPort();
        int value = hpMessage.getNodeType();
        if (!nodeMap.containsKey(key)) {
            nodeMap.put(key, value);
        }
        // print the new node map
        System.out.println("+++++++++++++New node map after added new node++++++++++++");
        setPrintNodeMap(true);
        // this is to get user input from other nodes after initiate new node
        setGetUserInput(true);
        System.out.println("bwefjwebfkwebfkwebfkwe" + isGetUserInput+ " " + isPrintNodeMap);

        //generate message to send acknowledgement to node type update message
        hpMessage.setEventType(MessageTags.EVENT_TYPE_AKC_FOR_NODE_TYPE_UPDATE);
        hpMessage.setReceiverPort(hpMessage.getSenderPort());
        hpMessage.setSenderPort(port);
        hpMessage.setNodeType(nodeMap.get(port));

        //send the populated message through a queue message
        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        JmsTemplate jmsTemplate = activeMQConfig.jmsTemplate();
        JmsMessageSender jmsMessageSender = new JmsMessageSender(jmsTemplate);
        System.out.println("Send an acknowledgement to node type update message to : " + hpMessage.getReceiverPort());
        jmsMessageSender.sendHPMessage(hpMessage);
    }

    public void finalizeNodeType(HPMessage hpMessage) {
        int key = hpMessage.getSenderPort();
        int value = hpMessage.getNodeType();
        if (!nodeMap.containsKey(key)) {
            nodeMap.put(key, value);
        }
        setPrintNodeMap(true);
        // this is to get user input from the new node after initiate a new node
        setGetUserInput(true);
    }

    public void printNodeMap() {
        System.out.println("Print Node Map ===============");
        for (Integer key : nodeMap.keySet()) {
            System.out.println("Key : " + key + " value : " + nodeMap.get(key));
        }
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getReplyCountForNodeInitiation() {
        return replyCountForNodeInitiation.get();
    }

    public boolean isPrintNodeMap() {
        return isPrintNodeMap;
    }

    public void setPrintNodeMap(boolean printNodeMap) {
        isPrintNodeMap = printNodeMap;
    }

    public Map<Integer, Integer> getNodeMap() {
        return nodeMap;
    }

    public boolean isGetUserInput() {
        return isGetUserInput;
    }

    public void setGetUserInput(boolean getUserInput) {
        isGetUserInput = getUserInput;
    }
}
