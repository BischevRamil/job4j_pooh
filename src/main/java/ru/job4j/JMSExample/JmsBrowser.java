package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Класс просмоторщика JMS сообщений JmsBrowser считывает сообщения с топика, не удаляя их. Реализует подписчика.
 */

public class JmsBrowser {
    private int processors = Runtime.getRuntime().availableProcessors();
    private ExecutorService pool = Executors.newFixedThreadPool(processors);
    private ArrayList<String> listOfMessages = new ArrayList<>();


    public static void main(String[] args) {
        new JmsBrowser().browseMessages();
    }

    private void browseMessages() {
        listOfMessages.add("GET /topic/weather/");
        listOfMessages.add("GET /topic/location/");

        for (String request : listOfMessages) {
            pool.execute(new Browser(request));
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

    private class Browser implements Runnable {
        private MessageClient messageClient;
        private String request;

        public Browser(String request) {
            messageClient = new MessageClient();
            this.request = request;
        }

        public void run() {
            if (messageClient.isConnected()) {
                try {
                    String response;
                    response = messageClient.sendRequest(request);
                    System.out.println("Message :" + response);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    messageClient.close();
                }
            }
        }
    }
}
