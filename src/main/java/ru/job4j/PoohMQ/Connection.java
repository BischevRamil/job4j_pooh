package ru.job4j.PoohMQ;

import ru.job4j.PoohMQ.Brocker.Brocker;

import java.io.IOException;

public class Connection implements AutoCloseable {
    private Session session;
    private String host;
    private int port;
    private Brocker brocker;

    public Connection(String host, int port) {
        this.host = host;
        this.port = port;
        this.brocker = new Brocker(this.host, this.port);
    }

//    public Session createSession() {
//        session = new Session();
//        return session;
//    }

    public void start() throws IOException {
        this.brocker.start();
    }

    @Override
    public void close() throws Exception {
        this.brocker.finish();
    }
}
