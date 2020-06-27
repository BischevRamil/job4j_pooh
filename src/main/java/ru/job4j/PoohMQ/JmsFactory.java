package ru.job4j.PoohMQ;

public class JmsFactory {

    private JmsFactory() {
    }

    public static JmsFactory getInstance() {
        return Holder.INSTANCE;
    }

    private static final class Holder {
        private static final JmsFactory INSTANCE = new JmsFactory();
    }

    public JmsConnectionFactory createConnectionFactory() {
        return new JmsConnectionFactory();
    }
}
