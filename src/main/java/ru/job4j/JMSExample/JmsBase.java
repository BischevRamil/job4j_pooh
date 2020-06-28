package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;
import java.io.IOException;

public abstract class JmsBase {
    private Connection connection;

    public JmsBase() throws IOException {
        createConnection();
        connection.start();
        doAction();
    }

    private void createConnection() {
        JmsFactory jmsFactory;
        JmsConnectionFactory jmsConnectionFactory;
        try {
            jmsFactory = JmsFactory.getInstance();
            jmsConnectionFactory = jmsFactory.createConnectionFactory();
            jmsConnectionFactory.setHost("localhost");
            jmsConnectionFactory.setPort(8080);
            connection = jmsConnectionFactory.createConnection();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //============================================================
    public abstract void doAction();
}
