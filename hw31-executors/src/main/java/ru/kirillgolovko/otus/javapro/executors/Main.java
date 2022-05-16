package ru.kirillgolovko.otus.javapro.executors;

/**
 * @author kirillgolovko
 */
public class Main {
    public static void main(String[] args) {
        TwoFlipFlopPrintingThreads instance = TwoFlipFlopPrintingThreads.getInstance("first", "second", 1, 10);
        instance.start();
    }
}
