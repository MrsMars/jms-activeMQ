package sender;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class UnidentifiedProducer {
    private static final Logger log =
            LoggerFactory.getLogger(UnidentifiedProducer.class);

    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public void create(String destinationName) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        messageProducer = session.createProducer(null);
    }

    public void close() throws JMSException {
        connection.close();
    }

    public void sendName(String destinationName, String firstName, String lastName) throws JMSException {
        String text = firstName + " " + lastName;

        TextMessage textMessage = session.createTextMessage(text);
        Destination destination = session.createQueue(destinationName);

        // send the message to the queue destination
        messageProducer.send(destination, textMessage);
        log.debug("unidentifiedProducer sent message with text='{}'", text);
    }
}
