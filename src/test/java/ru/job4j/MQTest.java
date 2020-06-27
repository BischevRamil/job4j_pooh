package ru.job4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import ru.job4j.PoohMQ.MainServer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class MQTest {
    private ConcurrentHashMap<String, LinkedList<String>> queueTest;
    private MainServer server;

    @Before
    public void setUp() {
        server = new MainServer(5000, 5001);
        server.run();
    }

    @Test
    public void test() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 5000);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        Thread.sleep(1000);
        out.write("{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}");
        out.flush();
        out.close();
        Thread.sleep(1000);
        queueTest = server.getQueue();
        assertThat(queueTest.get("queue").poll(), is("{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}"));
    }
}
