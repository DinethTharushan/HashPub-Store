package com.jmsmessagetester.jmsmessagetester;

import com.jmsmessagetester.jmsmessagetester.logic.MessageRouter;
import com.jmsmessagetester.jmsmessagetester.service.LogService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.CountDownLatch;

@EnableScheduling
@SpringBootApplication
public class JmsmessagetesterApplication {

	private static final CountDownLatch latch = new CountDownLatch(1);

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(JmsmessagetesterApplication.class, args);
		Environment environment = context.getBean(Environment.class);
		MessageRouter messageRouter = new MessageRouter(environment);
		messageRouter.startNode();
		System.out.println("Node Up and Running");
//		LogService logService = new LogService();
//		logService.sendLog("my name is dineth.");
	}

}
