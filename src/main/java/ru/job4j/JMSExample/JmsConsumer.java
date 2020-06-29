package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/*
 * Класс считывает сообщение из своей очереди и удаляет его, затем следующее, и так далее пока очередь
 * не станет пустой. После этого завершает соединение.
 */
public class JmsConsumer {
    private int processors = Runtime.getRuntime().availableProcessors();
    private ExecutorService pool = Executors.newFixedThreadPool(processors);
    private ArrayList<String> listOfMessages = new ArrayList<>();

    public static void main(String[] args) {
        new JmsConsumer().readMessages();
    }

    private void readMessages() {
        listOfMessages.add("GET /queue/weather/");
        listOfMessages.add("GET /queue/location/");

        for (String request : listOfMessages) {
            pool.execute(new Consumer(request));
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

    private class Consumer implements Runnable {
        private MessageConsumer consumer = null;
        private String request;

        public Consumer(String request) {
            consumer = new MessageConsumer();
            this.request = request;
        }

        public void run() {
            String message;
            try {
                do {
                    message = consumer.receive(request);
                    if (message != null)
                        System.out.println("Сообщение :" + message);
                } while (message != null);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                consumer.close();
            }
            System.out.println("Просмотр JMS сообщений завершен");
        }
    }
}
