package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

/*
 * Класс считывает сообщение из своей очереди и удаляет его, затем следующее, и так далее пока очередь
 * не станет пустой. После этого ожидает новых сообщений.
 */
public class JmsConsumer {
    private MessageConsumer consumer = null;

    public JmsConsumer() {
        consumer = new MessageConsumer();
        doAction();
    }

    private void doAction() {
        String message;
        try {

            do {
                message = consumer.receive();
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

    public static void main(String[] args) {
        new JmsConsumer();
    }
}
