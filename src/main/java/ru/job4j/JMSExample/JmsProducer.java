package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.Message;
import ru.job4j.PoohMQ.MessageProducer;

import java.io.IOException;

public class JmsProducer extends JmsBase {
    private MessageProducer producer = null;

    public JmsProducer() throws IOException {
        super();
        if (producer != null) {
            producer.close();
        }
    }

    @Override
    public void doAction() {
        String message = "{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}";
        try {
            Message m = null;
            producer = session.createProducer(destination);
            System.out.println("Отправка JMS сообщений");
            m = session.createMessage(message);
            producer.send(m);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JmsProducer();
    }
}
