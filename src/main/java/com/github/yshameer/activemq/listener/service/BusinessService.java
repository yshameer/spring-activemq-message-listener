package com.github.yshameer.activemq.listener.service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BusinessService {
    public String doSomethingWithMessage(String message) {
        log.info("doSomethingWithMessage {}", message);
        return "Successfully read message " + message;
    }
}
