package ru.job4j.PoohMQ;

import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class ServerOut implements Runnable {
    private Socket socket;
    private ConcurrentHashMap<String, LinkedList<String>> queue;

    public ServerOut(Socket socket, ConcurrentHashMap<String, LinkedList<String>> queue) {
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run() {

    }
}
