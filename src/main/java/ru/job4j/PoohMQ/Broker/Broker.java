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

public class Broker implements Runnable {
    private ConcurrentHashMap<String,MyQueue> queueList;
    private Selector selector;
    private InetSocketAddress address;
    private Set<SocketChannel> session;
    boolean isRunning = true;

    public Broker(String host, int port) {
        this.address = new InetSocketAddress(host, port);
        this.session = new HashSet<>();
        this.queueList = new ConcurrentHashMap<>();
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
        broadcast("System:new client: " + channel.socket().getRemoteSocketAddress() + "\n");
    }

    private void read(SelectionKey key) throws IOException {
        SocketChannel channel = (SocketChannel) key.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numRead = channel.read(byteBuffer);
        if (numRead == -1) {
            this.session.remove(channel);
            broadcast("System:client left: " + channel.socket().getRemoteSocketAddress() + "\n");
            channel.close();
            key.cancel();
            return;
        }

        byte[] data = new byte[numRead];
        System.arraycopy(byteBuffer.array(), 0, data, 0, numRead);
        String gotData = new String(data);

        System.out.println("Got: " + gotData);
        broadcast(channel.socket().getRemoteSocketAddress() + ": " + gotData);
    }

    private void write(SelectionKey key) throws IOException {

    }

    private void broadcast(String data) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        byteBuffer.put(data.getBytes());
        byteBuffer.flip();
        this.session.forEach(socketChannel -> {
            try {
                socketChannel.write(byteBuffer);
                byteBuffer.flip();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void finish() throws IOException {
        this.isRunning = false;
        this.selector.close();
    }
}
