package ru.kirillgolovko.otus.java.hw33.client;

import java.util.Optional;

/**
 * @author kirillgolovko
 */
public interface NumbersStreamClient extends AutoCloseable {
    void startGettingNumbers(long from, long to);
    Optional<Long> getLastNumber();
}
