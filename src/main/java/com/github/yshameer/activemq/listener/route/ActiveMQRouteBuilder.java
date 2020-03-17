package com.github.yshameer.activemq.listener.route;

import com.github.yshameer.activemq.listener.config.ListenerProperties;
import com.github.yshameer.activemq.listener.listener.ActiveMQMessageListener;
import lombok.RequiredArgsConstructor;
import org.apache.camel.ExchangePattern;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.DefaultErrorHandlerBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.processor.RedeliveryPolicy;

import static java.lang.String.format;

@RequiredArgsConstructor
public class ActiveMQRouteBuilder extends RouteBuilder {
    private final ActiveMQMessageListener messageListener;
    private final RedeliveryPolicy redeliveryPolicy;
    private final ListenerProperties listenerProperties;

    @Override
    public void configure() {
        onException(Exception.class)
            .redeliveryPolicy(redeliveryPolicy);

        final DefaultErrorHandlerBuilder errorHandlerBuilder = deadLetterChannel(format("jms:queue:%s", listenerProperties.getDeadLetterQueueName()))
            .maximumRedeliveries(redeliveryPolicy.getMaximumRedeliveries())
            .useOriginalMessage()
            .retryAttemptedLogLevel(LoggingLevel.ERROR)
            .logExhausted(true)
            .logHandled(true)
            .logNewException(true)
            .logRetryStackTrace(true)
            .logRetryAttempted(true)
            .logExhaustedMessageHistory(true);

        from(format("jms:queue:%s?cacheLevelName=CACHE_CONSUMER", listenerProperties.getQueueName()))
            .errorHandler(errorHandlerBuilder)
            .transacted()
            .threads(listenerProperties.getNumOfThreads())
            .setExchangePattern(ExchangePattern.InOnly)
            .bean(messageListener, "receiveMessage")
        ;

    }
}
