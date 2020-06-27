package ru.job4j.PoohMQ;

public class JmsConnectionFactory {
    private Connection connection;

    public JmsConnectionFactory() {

    }

    public Connection createConnection() {
        return connection;
    }
}
