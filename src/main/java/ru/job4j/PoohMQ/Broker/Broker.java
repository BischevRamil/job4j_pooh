package ru.job4j.PoohMQ.Broker;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bischev Ramil
 * @since 2020-06-29
 *
 */
public class Broker implements Runnable {
    private ConcurrentHashMap<String, MyQueue> queueList;
    private ConcurrentHashMap<String, MyQueue> topicList;
    private Selector selector;
    private InetSocketAddress address;
    private Set<SocketChannel> session;
    boolean isRunning = true;

    public Broker(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        this.queueList = new ConcurrentHashMap<>();
        this.topicList = new ConcurrentHashMap<>();
    }

    public void run() {
        try {
            this.selector = Selector.open();
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.bind(address);
            serverSocketChannel.configureBlocking(false);
            serverSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);

            System.out.println("Server started...");

            while(isRunning) {
                // blocking, wait for events
                this.selector.select();
                Iterator keys = this.selector.selectedKeys().iterator();
                while(keys.hasNext()) {
                    SelectionKey key = (SelectionKey) keys.next();
                    keys.remove();
                    if (!key.isValid()) {
                        continue;
                    }
                    if (key.isAcceptable()) {
                        accept(key);
                    }
                    else if (key.isReadable()) {
                        read(key);
                    }
                    else if (key.isWritable()) {
                        write(key);
                    }
                }
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

    }

    private void accept(SelectionKey key) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
        SocketChannel channel = serverSocketChannel.accept();
        channel.configureBlocking(false);
        channel.register(this.selector, SelectionKey.OP_READ);
        this.session.add(channel);
        System.out.println("System:new client: " + channel.socket().getRemoteSocketAddress() + "\n");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = channel.read(byteBuffer);
        if (numRead == -1) {
            this.session.remove(channel);
            System.out.println("System:client left: " + channel.socket().getRemoteSocketAddress() + "\n");
            channel.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
        String gotData = new String(data);
        System.out.println("Got: " + gotData);
        byteBuffer.clear();
        Request request = new Request(gotData);

        /*запрос на размещение записи */
        if (request.getTypeOfRequest().equalsIgnoreCase("POST")) {

            /*Помещение в очередь*/
            if (request.getTypeOfQueue().equalsIgnoreCase("Queue")) {
                if (this.queueList.containsKey(request.getTheme())) {
                    this.queueList.get(request.getTheme()).putAtHeader(request.getBody());
                } else {
                    MyQueue myQueue = new MyQueue();
                    myQueue.putAtHeader(request.getBody());
                    this.queueList.put(request.getTheme(), myQueue);
                }
                /*Помещение в топик*/
            } else if (request.getTypeOfQueue().equalsIgnoreCase("Topic")) {
                if (this.topicList.containsKey(request.getTheme())) {
                    this.topicList.get(request.getTheme()).putAtHeader(request.getBody());
                } else {
                    MyQueue myQueue = new MyQueue();
                    myQueue.putAtHeader(request.getBody());
                    this.topicList.put(request.getTheme(), myQueue);
                }
            }
            System.out.println("queue: " + this.queueList);
            System.out.println("topic: " + this.topicList);
            this.session.remove(channel);
            channel.close();
            key.cancel();
            return;
        } else {
            channel.register(this.selector, SelectionKey.OP_WRITE, request);
        }
    }

    private void write(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        Request request = (Request) key.attachment();
        ByteBuffer byteBuffer;
        try {
            if(request.getTypeOfRequest().equalsIgnoreCase("GET")) {
                if (request.getTypeOfQueue().equalsIgnoreCase("Queue")) {
                    if (this.queueList.containsKey(request.getTheme())) {
                        byteBuffer = ByteBuffer.wrap(this.queueList.get(request.getTheme()).getAndRemoveTail().getBytes());
                        channel.write(byteBuffer);
                        byteBuffer.clear();
                    }
                } else if (request.getTypeOfQueue().equalsIgnoreCase("Topic")) {
                    if (this.topicList.containsKey(request.getTheme())) {
                        byteBuffer = ByteBuffer.wrap(this.topicList.get(request.getTheme()).getTail().getBytes());
                        channel.write(byteBuffer);
                        byteBuffer.clear();
                        channel.close();
                        key.cancel();
                    }
                }
            }
        } catch (NullPointerException npe) {
            channel.close();
            key.cancel();
            npe.printStackTrace();
            System.out.println("Queue is empty");
        }
    }
}
