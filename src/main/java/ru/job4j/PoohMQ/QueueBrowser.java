package ru.job4j.PoohMQ;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class QueueBrowser {
    private Socket socket;
    private String host = "localhost";
    private int port = 8080;
    private boolean isConnected;

    public QueueBrowser() {
        try {
            createSocket();
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.out.println("Client not connected to broker");
        }
    }

    private void createSocket() throws IOException {
        socket = new Socket(InetAddress.getByName(host), port);
        isConnected = true;
    }

    public String onMessage(String request) throws IOException {
        String message;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out.write(request);
        out.flush();
        message = in.readLine();
        System.out.println("message: " + message);
        return message;
    }

    public void close() {
        try {
            this.socket.close();
            isConnected = false;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
