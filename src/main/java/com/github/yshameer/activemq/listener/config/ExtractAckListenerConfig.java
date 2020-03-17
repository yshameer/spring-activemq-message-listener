package com.github.yshameer.activemq.listener.config;

import com.github.yshameer.activemq.listener.listener.ActiveMQMessageListener;
import com.github.yshameer.activemq.listener.route.ActiveMQRouteBuilder;
import com.github.yshameer.activemq.listener.service.BusinessService;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.processor.RedeliveryPolicy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.connection.JmsTransactionManager;

import javax.jms.ConnectionFactory;
import java.util.concurrent.TimeUnit;

@Configuration
public class ExtractAckListenerConfig {
    private static final int AUTO_ACKNOWLEDGEMENT_MODE = 0;

    @Bean
    public RedeliveryPolicy redeliveryPolicy(RedeliveryProperties redeliveryProperties) {
        final org.apache.camel.processor.RedeliveryPolicy redeliveryPolicy = new org.apache.camel.processor.RedeliveryPolicy()
                .redeliveryDelay(TimeUnit.SECONDS.toMillis(redeliveryProperties.getInitialDelay()))
                .backOffMultiplier(redeliveryProperties.getBackOffMultiplier())
                .maximumRedeliveryDelay(TimeUnit.SECONDS.toMillis(redeliveryProperties.getMaxDelay()))
                .maximumRedeliveries(redeliveryProperties.getMaxRedeliveryCnt());
        redeliveryPolicy.setUseExponentialBackOff(redeliveryProperties.getUseExponentialBackOff());
        return redeliveryPolicy;
    }

    @Bean
    public RouteBuilder extractAckRouteBuilder(ActiveMQMessageListener messageListener, RedeliveryPolicy redeliveryPolicy, ListenerProperties listenerProperties) {
        return new ActiveMQRouteBuilder(messageListener, redeliveryPolicy, listenerProperties);
    }

    @Bean
    public JmsTransactionManager transactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

    @Bean
    public JmsComponent jmsComponent(ConnectionFactory connectionFactory, JmsTransactionManager transactionManager) {
        final JmsComponent jmsComponent = JmsComponent.jmsComponent(connectionFactory);
        jmsComponent.setAcknowledgementMode(AUTO_ACKNOWLEDGEMENT_MODE);
        return jmsComponent;
    }

    @Bean
    public BusinessService businessService(){
        return new BusinessService();
    }

}
