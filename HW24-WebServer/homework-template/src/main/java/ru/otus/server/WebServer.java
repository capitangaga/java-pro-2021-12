package ru.otus.server;

/**
 * @author kirillgolovko
 */
public interface WebServer {
    void start() throws Exception;

    void join() throws Exception;

    void stop() throws Exception;
}
