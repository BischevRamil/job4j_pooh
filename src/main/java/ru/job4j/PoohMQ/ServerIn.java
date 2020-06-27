package ru.job4j.PoohMQ;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Read one message, put message in queue and finish job.
 */
public class ServerIn implements Runnable {
    private Socket socket;
    private BufferedReader in;
    private PrintWriter out;
    private ConcurrentHashMap<String, LinkedList<String>> queue;

    public ServerIn(Socket socket, ConcurrentHashMap<String, LinkedList<String>> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {
        try {
            readMessage();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readMessage() throws IOException {
        System.out.println("serverIn");
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
        try {
            String json = in.readLine();
            String nameQueue = getName(json);
            if (!queue.containsKey(nameQueue)) {
                queue.put(nameQueue, new LinkedList<>(List.of(json)));
            } else {
                queue.get(nameQueue).offer(json);
            }
        } finally {
            socket.close();
            in.close();
            out.close();
        }

    }

    private String getName(String json) {
        return json.replaceAll("[\"]", "/").split("/")[3];
    }
}
