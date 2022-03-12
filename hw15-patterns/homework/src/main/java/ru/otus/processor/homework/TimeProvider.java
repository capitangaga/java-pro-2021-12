package ru.otus.processor.homework;

import java.time.Instant;

/**
 * @author kirillgolovko
 */
public interface TimeProvider {
    Instant getTime();
}
