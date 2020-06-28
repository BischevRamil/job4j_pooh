package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.Broker.Broker;

public class BrokerStart {

    public static void main(String[] args) {
        new Thread(new Broker("localhost", 8080)).start();
    }
}
