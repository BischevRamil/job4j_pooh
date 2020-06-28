package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.MessageProducer;

public class JmsProducer {
    private MessageProducer producer = null;

    public JmsProducer() {
        producer = new MessageProducer();
        doAction();
    }

    private void doAction() {
        String message = "POST /queue \n {\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}";
        try {
            System.out.println("Отправка JMS сообщений");
            producer.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            producer.close();
        }
    }

    public static void main(String[] args) {
        new JmsProducer();
    }
}
