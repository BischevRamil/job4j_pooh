package ru.job4j.PoohMQ;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MessageConsumer {
    private Socket socket;
    private String host = "localhost";
    private int port = 8080;

    public MessageConsumer() {
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

    public String receive(String request) throws IOException {
        String message;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out.write(request);
        out.flush();
        message = in.readLine();
        return message;
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
