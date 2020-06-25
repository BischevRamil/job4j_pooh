package ru.job4j.PoohMQ;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

public class QueueMQ implements IMQ {
    private Socket socket;
    private boolean isRunning = true;
    //key=name of queue; value=queue;
    private ConcurrentHashMap<String, LinkedList<String>> hashMap = new ConcurrentHashMap<>();

    private QueueMQ(Socket socket) throws IOException {
        this.socket = socket;
        Thread inputThread = new Thread(new InputMessage(this.socket));
        Thread outputThread = new Thread(new OutputMessage());
        inputThread.start();
        outputThread.start();
    }

    public static QueueMQ createConnection() {
        QueueMQ instance = null;
        try(Socket socket = new ServerSocket(5000).accept()) {
            instance = new QueueMQ(socket);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        return instance;
    }

    private String getName(String json) {
        return json.replaceAll("\\W", "").split("")[1];
    }

    public void shutDown() {
        this.isRunning = false;
    }


    private class InputMessage implements Runnable {
        private Socket s;
        private InputStream is;

        private InputMessage(Socket s) throws IOException {
            this.s = s;
            this.is = s.getInputStream();
        }

        @Override
        public void run() {
            String json = null;
            try {
                BufferedReader br = new BufferedReader(new InputStreamReader(is));
                while(isRunning) {
                    json = br.readLine();
                    String name = getName(json);
                    if (!hashMap.containsKey(name)) {
                        hashMap.put(name, new LinkedList<>(List.of(json)));
                    } else {
                        hashMap.get(name).add(json);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private static class OutputMessage implements Runnable {

        @Override
        public void run() {

        }
    }
}
