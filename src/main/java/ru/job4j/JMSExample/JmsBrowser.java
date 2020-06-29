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
        private QueueBrowser browser;
        private String request;

        public Browser(String request) {
            browser = new QueueBrowser();
            this.request = request;
        }

        public void run() {
            try {
                String message;
                message = browser.onMessage(request);
                System.out.println("Сообщение :" + message);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                browser.close();
            }
        }
    }
}
