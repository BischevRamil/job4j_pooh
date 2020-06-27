package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

import java.util.Queue;

public abstract class JmsBase {
    private Connection connection;
    protected Session session;
    private String queueName = "queue1";
    protected Queue destination;

    public JmsBase() {
        try {
            createConnection();
            session = connection.createSession();
            destination = session.createQueue(queueName);
            connection.start();
            doAction();
        } finally {
            try {
                if (session != null)
                    session.close();
                if (connection != null)
                    connection.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void createConnection() {
        JmsFactory jmsFactory;
        JmsConnectionFactory jmsConnectionFactory;
        try {
            jmsFactory = JmsFactory.getInstance();
            jmsConnectionFactory = jmsFactory.createConnectionFactory();
            connection = jmsConnectionFactory.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //============================================================
    public abstract void doAction();
}
