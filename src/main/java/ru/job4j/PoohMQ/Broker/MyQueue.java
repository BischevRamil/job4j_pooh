package ru.job4j.PoohMQ.Broker;

import java.util.concurrent.ConcurrentLinkedDeque;

public class MyQueue {
    private ConcurrentLinkedDeque<String> queue;

    public MyQueue() {
        queue = new ConcurrentLinkedDeque<>();
    }

    public void putAtHeader(String value) {
        queue.addFirst(value);
    }

    public String getAndRemoveTail() {
        return queue.pollLast();
    }

    public String getTail() {
        return queue.peekLast();
    }

    public int size() {
        return queue.size();
    }
}
