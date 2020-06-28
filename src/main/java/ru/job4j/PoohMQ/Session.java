package ru.job4j.PoohMQ;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Session {

    public Session() {

    }

    public Queue<String> createQueue(String queue) {
        return new ConcurrentLinkedQueue<String>();
    }
}
