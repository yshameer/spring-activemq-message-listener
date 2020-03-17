package com.github.yshameer.activemq.listener.listener;

import com.github.yshameer.activemq.listener.config.ListenerProperties;
import com.github.yshameer.activemq.listener.service.BusinessService;
import org.apache.activemq.junit.EmbeddedActiveMQBroker;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.MessageConsumer;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles(profiles = "test")
@DirtiesContext
public class ActiveMQMessageListenerTest {


    @Rule
    public EmbeddedActiveMQBroker broker = new EmbeddedActiveMQBroker();

    @Autowired
    private ConnectionFactory connectionFactory;

    @Autowired
    private ListenerProperties listenerProperties;

    @MockBean
    private BusinessService businessService;


    private Destination destination;
    private Session session;
    private MessageProducer producer;
    private MessageConsumer deadLetterConsumer;


    @Before
    public void setUp() throws JMSException {
        Connection conn = connectionFactory.createConnection();
        conn.start();
        session = conn.createSession(false, Session.AUTO_ACKNOWLEDGE);
        destination = session.createQueue(listenerProperties.getQueueName());
        Destination deadLetterDestination = session.createQueue(listenerProperties.getDeadLetterQueueName());

        this.producer = session.createProducer(destination);
        this.producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
        this.deadLetterConsumer = session.createConsumer(deadLetterDestination);
    }

    @Test
    public void shouldReceiveMessageAndCallService() throws Exception {

        producer.send(destination, session.createObjectMessage("Success Message"));
        TimeUnit.SECONDS.sleep(10);//wait for MessageListener to pick up message from queue

        verify(businessService).doSomethingWithMessage("Success Message");

    }

    @Test
    public void shouldRetryInCaseOfException() throws Exception {

        when(businessService.doSomethingWithMessage(any())).thenThrow(RuntimeException.class);
        producer.send(destination, session.createObjectMessage("Retry Message"));
        TimeUnit.SECONDS.sleep(10);//wait for eMessageListener to pick up message from queue

        verify(businessService, times(3)).doSomethingWithMessage("Retry Message");
    }

    @Test
    public void shouldMoveToDeadLetterQueueWhenRetriesExhausted() throws Exception {

        when(businessService.doSomethingWithMessage(any())).thenThrow(RuntimeException.class);
        producer.send(destination, session.createObjectMessage("DeadLetter Message"));
        TimeUnit.SECONDS.sleep(10);//wait for MessageListener to pick up message from queue

        final TextMessage actualMessage = (TextMessage) deadLetterConsumer.receive();
        assertEquals("DeadLetter Message", actualMessage.getText());
    }


}
