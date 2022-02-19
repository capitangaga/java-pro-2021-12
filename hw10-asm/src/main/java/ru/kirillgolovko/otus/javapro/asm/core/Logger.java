package ru.kirillgolovko.otus.javapro.asm.core;

import java.util.stream.Stream;

/**
 * @author kirillgolovko
 */
public class Logger {
    public static void log(String methodName, Object... params) {
        System.out.printf(
                "Invoked method: %s, params: %s%n",
                methodName,
                String.join(", ", Stream.of(params).map(Object::toString).toList()));
    }
}
