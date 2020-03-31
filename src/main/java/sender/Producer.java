package sender;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jms.*;

public class Producer {

    private static final Logger log =
            LoggerFactory.getLogger(Producer.class);

    private Connection connection;
    private Session session;
    private MessageProducer messageProducer;

    public void create(String destinationName) throws JMSException {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(ActiveMQConnectionFactory.DEFAULT_BROKER_URL);
        connection = connectionFactory.createConnection();
        session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue(destinationName);
        messageProducer = session.createProducer(destination);
    }

    public void close() throws JMSException {
        connection.close();
    }

    public void sendName(String firstName, String lastName) throws JMSException {
        String text = firstName + " " + lastName;

        TextMessage textMessage = session.createTextMessage(text);
        messageProducer.send(textMessage);
        log.debug("producer sent message with text='{}'", text);
    }
}
