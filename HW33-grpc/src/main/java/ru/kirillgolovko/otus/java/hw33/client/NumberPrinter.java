package ru.kirillgolovko.otus.java.hw33.client;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @author kirillgolovko
 */
public class NumberPrinter {
    public static void printNumbers(long from, long to, Supplier<Optional<Long>> numbersStream) {
        long currentNumber = 0;
        for(long i = from; i <= to; ++i) {
            currentNumber = currentNumber + numbersStream.get().orElse(0L) + 1;
            System.out.printf("[%s] Current number: %d\n", Thread.currentThread().getName(), currentNumber);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
