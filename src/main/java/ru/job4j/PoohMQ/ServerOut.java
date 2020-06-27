package ru.job4j.PoohMQ;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Read request from client, if name of client is valid then send message to him.
 */
public class ServerOut implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ConcurrentHashMap<String, LinkedList<String>> queue;

    public ServerOut(Socket socket, ConcurrentHashMap<String, LinkedList<String>> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            pollMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void pollMessage() throws IOException {
        System.out.println("serverOut");
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        try {
            String request = in.readLine();
            String nameQueue = getName(request);
            if (queue.containsKey(nameQueue)) {
                out.write(Objects.requireNonNull(queue.get(nameQueue).poll()));
            } else {
                System.out.println("Such queue not available");
            }
        } finally {
            socket.close();
            in.close();
            out.close();
        }
    }

    private String getName(String request) {
        return request.split("/")[2];
    }
}
