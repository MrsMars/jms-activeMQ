package consumer;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class Receiver {

    private static final Logger log = LoggerFactory.getLogger(Receiver.class);

    private static final String NO_GREETING = "no greeting";

    private Connection connection;
    private MessageConsumer messageConsumer;

    public void create(String destinationName) throws JMSException {
        ConnectionFactory connectionFactory =
                new ActiveMQConnectionFactory(
                        ActiveMQConnectionFactory.DEFAULT_BROKER_URL
                );

        connection = connectionFactory.createConnection();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination destination = session.createQueue(destinationName);
        messageConsumer = session.createConsumer(destination);
        connection.start();
    }

    public void close() throws JMSException {
        connection.close();
    }

    public String getGreeting(int timeout) throws JMSException {
        String greeting = NO_GREETING;

        Message message = messageConsumer.receive(timeout);
        if (message != null) {
            TextMessage textMessage = (TextMessage) message;

            String text = textMessage.getText();
            log.debug("consumer received message with text='{}'", text);

            greeting = "Hello " + text + "!";
        } else {
            log.debug("consumer received no message");
        }

        log.info("greeting={}", greeting);
        return greeting;
    }




}
