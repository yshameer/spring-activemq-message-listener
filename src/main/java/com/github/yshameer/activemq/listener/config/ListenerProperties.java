package com.github.yshameer.activemq.listener.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "listener.config")
@Data
@Validated
public class ListenerProperties {
    @NotNull
    private String queueName;
    @NotNull
    private String deadLetterQueueName;
    @NotNull
    private Integer numOfThreads;
}
