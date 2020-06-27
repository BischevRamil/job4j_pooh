package ru.job4j.PoohMQ;

public class Connection {
    private Session session;

    public Connection() {

    }

    public Session createSession() {
        return session;
    }
}
