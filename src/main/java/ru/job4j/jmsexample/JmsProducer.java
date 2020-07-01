package ru.job4j.jmsexample;

import ru.job4j.poohmq.MessageClient;

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
        private MessageClient messageClient = null;
        private String message;

        public Publisher(String m) {
            messageClient = new MessageClient();
            message = m;
        }

        public void run() {
            if (messageClient.isConnected()) {
                String response;
                try {
                    System.out.println("Sending JMS message...");
                    response = messageClient.sendRequest(message);
                    System.out.println("Response from server: " + response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    messageClient.close();
                }
            }

        }
    }
}
