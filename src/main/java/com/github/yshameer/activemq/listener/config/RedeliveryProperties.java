package com.github.yshameer.activemq.listener.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Component
@ConfigurationProperties(prefix = "listener.redelivery.config")
@Data
@Validated
public class RedeliveryProperties {
    @NotNull
    private Long initialDelay;
    @NotNull
    private Long backOffMultiplier;
    @NotNull
    private Long maxDelay;
    @NotNull
    private Boolean useExponentialBackOff;
    @NotNull
    private Integer maxRedeliveryCnt;
}
