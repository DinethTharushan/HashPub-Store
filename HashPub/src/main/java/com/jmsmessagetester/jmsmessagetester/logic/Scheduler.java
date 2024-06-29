package com.jmsmessagetester.jmsmessagetester.logic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
public class Scheduler {

    @Autowired
    private NodeInitiater nodeInitiater;
    @Autowired
    private DataRouter dataRouter;
    @Autowired
    private DataStore dataStore;



    @Scheduled(initialDelay = 3000, fixedDelay = Long.MAX_VALUE)
    public void callAssignType() {
        nodeInitiater.assignType();
    }

    @Scheduled(fixedRate = 3000)
    public void printNodeMapWhenUpdated() {
        if (nodeInitiater.isPrintNodeMap()) {
            nodeInitiater.printNodeMap();
            nodeInitiater.setPrintNodeMap(false);
        } else if (nodeInitiater.isGetUserInput()) {
            CompletableFuture.runAsync(() -> dataRouter.getUserInput());
            nodeInitiater.setGetUserInput(false);
        } else if (dataStore.isDisplayRetreivedData()) {
            dataStore.displayRetreivedData();
            dataStore.setDisplayRetreivedData(false);
        }
    }

}
