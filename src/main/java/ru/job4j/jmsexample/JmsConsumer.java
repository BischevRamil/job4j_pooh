package ru.job4j.jmsexample;

import ru.job4j.poohmq.*;

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
        private MessageClient messageClient = null;
        private String request;

        public Consumer(String request) {
            messageClient = new MessageClient();
            this.request = request;
        }

        public void run() {
            if (messageClient.isConnected()) {
                String response;
                try {
                    do {
                        response = messageClient.sendRequest(request);
                        if (response != null) {
                            System.out.println("Message :" + response);
                        } else {
                            System.out.println("Queue is empty!");
                        }
                    } while (response != null);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    messageClient.close();
                }
                System.out.println("Message view complete");
            }
        }
    }
}
