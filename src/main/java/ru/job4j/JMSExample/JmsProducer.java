package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.MessageProducer;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Класс подключается к брокеру сообщений и отправляет сообщения. После чего завершает работу.
 */
public class JmsProducer {
    private int processors = Runtime.getRuntime().availableProcessors();
    private ExecutorService pool = Executors.newFixedThreadPool(processors);
    private ArrayList<String> listOfMessages = new ArrayList<>();

    public static void main(String[] args) {
        new JmsProducer().publicMessages();
    }

    public void publicMessages() {
        listOfMessages.add("POST /queue/weather/ {\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}");
        listOfMessages.add("POST /queue/location/ {\"queue\" : \"location\",\"text\" : \"Russia\"}");
        listOfMessages.add("POST /topic/weather/ {\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}");
        listOfMessages.add("POST /topic/location/ {\"queue\" : \"location\",\"text\" : \"Moscow\"}");

        for (String listOfMessage : listOfMessages) {
            pool.execute(new Publisher(listOfMessage));
        }

        pool.shutdown();
        while (!pool.isTerminated()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private class Publisher implements Runnable {
        private MessageProducer producer = null;
        private String message;

        public Publisher(String m) {
            producer = new MessageProducer();
            message = m;
        }

        public void run() {
            try {
                System.out.println("Отправка JMS сообщений");
                producer.send(message);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                producer.close();
            }
        }
    }
}
