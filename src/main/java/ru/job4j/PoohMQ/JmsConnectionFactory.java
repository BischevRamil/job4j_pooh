package ru.job4j.PoohMQ;

public class JmsConnectionFactory {
    private Connection connection;
    private String host;
    private int port;
    private final String DEFAULT_HOST = "localhost";
    private final int DEFAULT_PORT = 8080;

    public JmsConnectionFactory() {
        this.host = DEFAULT_HOST;
        this.port = DEFAULT_PORT;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Connection createConnection() {
        connection = new Connection(this.host, this.port);
        return connection;
    }
}
