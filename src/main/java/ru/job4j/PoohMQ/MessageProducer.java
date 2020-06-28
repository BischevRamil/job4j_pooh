package ru.job4j.PoohMQ;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;

public class MessageProducer {
    private Socket socket;
    private String host = "localhost";
    private int port = 8080;

    public MessageProducer() {
        try {
            createSocket();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client not connected to broker");
        }
    }

    private void createSocket() throws IOException {
        socket = new Socket(InetAddress.getByName(host), port);
    }


    public void send(String message) throws IOException {
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        out.write(message);
        out.flush();
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
