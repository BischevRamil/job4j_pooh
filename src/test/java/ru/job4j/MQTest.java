package ru.job4j;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.Matchers.is;

import ru.job4j.PoohMQ.MainServer;

import java.io.*;
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
    public void testMessageIn() throws IOException, InterruptedException {
        Socket socket = new Socket("localhost", 5000);
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        out.write("{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}");
        out.flush();
        out.close();
        Thread.sleep(1000);
        queueTest = server.getQueue();
        assertThat(queueTest.get("weather").peek(), is("{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}"));
    }

//    @Test
//    public void testMessageOut() throws IOException, InterruptedException {
//        Socket socket = new Socket("localhost", 5000);
//        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
//        out.write("GET /queue/weather");
//        out.flush();
//        Thread.sleep(1000);
//        String response = in.readLine();
//        assertThat(response, is("{\"queue\" : \"weather\",\"text\" : \"temperature +18 C\"}"));
//    }
}
