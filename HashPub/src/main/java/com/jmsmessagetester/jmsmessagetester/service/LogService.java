package com.jmsmessagetester.jmsmessagetester.service;

import com.jmsmessagetester.jmsmessagetester.ActiveMQConfig;
import com.jmsmessagetester.jmsmessagetester.logic.NodeInitiater;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@Component
public class LogService {

    @Autowired
    private Environment environment;

    public void sendLog(String logMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(logMessage, headers);
        int port = 9000 + 1;
        String url = "http://localhost:" + port + "/log";

        ActiveMQConfig activeMQConfig = new ActiveMQConfig();
        RestTemplate restTemplate = activeMQConfig.restTemplate();

        restTemplate.postForObject(url, request, Void.class);
    }
}
