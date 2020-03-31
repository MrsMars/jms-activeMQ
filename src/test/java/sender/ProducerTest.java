package sender;

import consumer.Receiver;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import javax.jms.JMSException;

import static org.junit.Assert.*;

public class ProducerTest {

    public static final String DESTINATION_NAME = "helloworld.q";

    private static Producer producer;
    private static Receiver receiver;

    @BeforeClass
    public static void setUpBeforeClass() throws JMSException {
        producer = new Producer();
        producer.create(DESTINATION_NAME);

        receiver = new Receiver();
        receiver.create(DESTINATION_NAME);
    }

    @AfterClass
    public static void tearDownAfterClass() throws JMSException {
        producer.close();
        receiver.close();
    }

    @Test
    public void testGetGreeting() {
        String firstName = "John";
        String lastName = "Doe";

        try {
            producer.sendName(firstName, lastName);

            String greeting = receiver.getGreeting(1000);
            assertEquals(String.format("Hello %s %s!", firstName, lastName), greeting);
        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }

    @Test
    public void testNoGreeting() {
        try {
            String greeting = receiver.getGreeting(1000);
            assertEquals("no greeting", greeting);
        } catch (JMSException e) {
            fail("a JMS Exception occurred");
        }
    }
}