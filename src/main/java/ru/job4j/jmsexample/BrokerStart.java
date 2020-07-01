package ru.job4j.jmsexample;

import ru.job4j.poohmq.broker.Broker;

public class BrokerStart {

    public static void main(String[] args) {
        new Thread(new Broker("localhost", 8080)).start();
    }
}
