package ru.kirillgolovko.otus.java.hw33.server;

import java.io.IOException;

import io.grpc.ServerBuilder;

/**
 * @author kirillgolovko
 */
public class ServerMain {

    private static final int PORT = 8081;

    public static void main(String[] args) throws IOException, InterruptedException {
        var numbersServer = new NumbersServerImpl();
        var server = ServerBuilder.forPort(PORT).addService(numbersServer).build();
        server.start();
        System.out.printf("Listening %s", PORT);
        server.awaitTermination();
    }
}
