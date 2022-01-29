package com.finalexam.DB;


import javax.annotation.Resource;
import javax.ejb.Stateless;
import javax.jms.Session;
import javax.jms.*;

@Stateless
public class QueueMessage {

    @Resource
    private ConnectionFactory connectionFactory;

    @Resource
    private Queue queue;


    // Send message to queue
    public void sendMessage(String text) throws JMSException {
        Connection connection = null;
        javax.jms.Session session = null;
        try{
            connection = connectionFactory.createConnection();
            connection.start();
            session = connection.createSession(false, javax.jms.Session.AUTO_ACKNOWLEDGE);

            MessageProducer producer =  session.createProducer(queue);

            producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

            TextMessage textMessage = session.createTextMessage(text);
            producer.send(textMessage);

        } finally {
            if (session != null) session.close();
            if (connection != null ) connection.close();
        }
    }

    public String receiveMessage() throws JMSException{
        Connection connection = null;
        javax.jms.Session session = null;
        MessageConsumer consumer = null;
        try {
            connection = connectionFactory.createConnection();
            connection.start();

            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            consumer = session.createConsumer(queue);

            TextMessage message = (TextMessage) consumer.receive(1000);

            return message.getText();
        } finally {
            if (consumer != null) consumer.close();
            if (session != null) session.close();
            if (connection != null) connection.close();
        }
    }

}
