package ru.job4j.poohmq;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class MessageClient {
    private Socket socket;
    private String host = "localhost";
    private int port = 8080;
    boolean isConnected = true;

    public MessageClient() {
        try {
            createSocket();
        } catch (IOException ioe) {
            isConnected = false;
            ioe.printStackTrace();
            System.out.println("Client not connected to broker");
        }
    }

    private void createSocket() throws IOException {
        socket = new Socket(InetAddress.getByName(host), port);
    }


    public String sendRequest(String request) throws IOException {
        String m;
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
        BufferedReader in = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        out.write(request);
        out.flush();
        m = in.readLine();
        return m;
    }

    public void close() {
        try {
            this.socket.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public boolean isConnected() {
        return isConnected;
    }
}
