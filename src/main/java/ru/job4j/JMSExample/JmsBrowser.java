package ru.job4j.JMSExample;

import ru.job4j.PoohMQ.*;

/*
 * Класс просмоторщика JMS сообщений JmsBrowser считывает сообщения с топика, не удаляя их. Реализует подписчика.
 */

public class JmsBrowser {
    private QueueBrowser browser = null;

    public JmsBrowser() {
        browser = new QueueBrowser();
        doAction();
    }

    private void doAction() {
        try {

            String message;
            while (browser.isConnected()) {
                message = browser.onMessage();
                System.out.println(message);
                Thread.sleep(10000);
                browser.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            browser.close();
        }
    }

    public static void main(String[] args) {
        new JmsBrowser();
    }
}
