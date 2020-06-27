package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

public class JmsConsumer extends JmsBase {
    private MessageConsumer consumer = null;

    public JmsConsumer() {
        super();
        if (consumer != null) {
            consumer.close();
        }
    }
    @Override
    public void doAction() {
        Message message;
        try {
            consumer = session.createConsumer(destination);
            do {
                message = consumer.receive();
                if (message != null)
                    System.out.println("Сообщение :" + message);
            } while (message != null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Просмотр JMS сообщений завершен");
    }

    public static void main(String[] args) {
        new JmsConsumer();
    }
}
