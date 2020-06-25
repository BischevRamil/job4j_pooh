package ru.job4j.Jms;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Abstract class create connection.
 */
public abstract class JmsBase {
    private String localhost = "localhost";
    private int port = 5000;
    private Socket socket;

    protected abstract void doAction();

    public JmsBase() {
        createConnection();
    }

    private void createConnection() {
        try {
            this.socket = new Socket(InetAddress.getByName(localhost), port);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
