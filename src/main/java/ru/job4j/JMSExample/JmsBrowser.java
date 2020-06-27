package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

import java.util.Enumeration;
/*
    Класс просмоторщика JMS сообщений JmsBrowser наследует все свойства родителя JmsBase и
    определяет процедуру doAction(). В конструкторе класса выполняется подключение к провайдеру MQ,
    после чего в процедуре doAction() создается объект просмотра сообщений browser типа QueueBrowser и
    выполняется чтение сообщений messages. Сообщения выводятся в консоль и выполнение процедуры завершается.
 */

public class JmsBrowser extends JmsBase {
    private QueueBrowser browser = null;

    public JmsBrowser() {
        super();
        if (browser != null) {
            browser.close();
        }
    }

    @Override
    public void doAction() {
        try {
            browser = session.createBrowser(destination);
            Enumeration<?> messages = browser.getEnumeration();
            Message message;
            while (messages.hasMoreElements()) {
                message = (Message) messages.nextElement();
                System.out.println(message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new JmsBrowser();
    }
}
