package com.github.yshameer.activemq.listener.listener;

import com.github.yshameer.activemq.listener.service.BusinessService;
import lombok.extern.slf4j.Slf4j;
import org.apache.camel.Exchange;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
public class ActiveMQMessageListener {

    private final BusinessService businessService;

    public ActiveMQMessageListener(BusinessService businessService){
        this.businessService = businessService;
    }

    public void receiveMessage(Exchange exchange) throws IOException {
        String message = exchange.getMessage().getBody(String.class);
        log.info("received message {}", message);
        businessService.doSomethingWithMessage(message);
    }
}
