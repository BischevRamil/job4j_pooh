package ru.job4j.PoohMQ;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class MainServer {
    private int portIn, portOut;
    private boolean isRunning = true;
    private ConcurrentHashMap<String, LinkedList<String>> queue = new ConcurrentHashMap<>();

    public ConcurrentHashMap<String, LinkedList<String>> getQueue() {
        return queue;
    }

    public void shutDown() {
        this.isRunning = false;
    }

    public MainServer(int portIn, int portOut) {
        this.portIn = portIn;
        this.portOut = portOut;
    }

    public void run() {
        Thread producerServerThread = new Thread(new ClientServer(this.portIn));
        Thread consumerServerThread = new Thread(new ClientServer(this.portOut));
        producerServerThread.start();
        consumerServerThread.start();
    }

    //создается поток для каждого клиента
    private void createServer(int port) throws IOException {
        try (ServerSocket s = new ServerSocket(port)) {
            System.out.println("Мультипоточный сервер стартовал");
            while (isRunning) {
                Socket socket = s.accept();
                System.out.println("Новое соединение установлено");
                if (port == this.portIn) {
                    Thread serverIn = new Thread(new ServerIn(socket, queue));
                    serverIn.start();
                } else {
                    Thread serverOut = new Thread(new ServerOut(socket, queue));
                    serverOut.start();
                }
            }
        }
    }

    public class ClientServer implements Runnable {
        //порт для работы с клиентом. В зависимости от порта, это или поставщик или подписчик.
        private int port;

        public ClientServer(int port) {
            this.port = port;
        }

        @Override
        public void run() {
            try {
                createServer(this.port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
