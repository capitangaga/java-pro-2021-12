package ru.kirillgolovko.otus.javapro.executors;

/**
 * @author kirillgolovko
 */
public class Main {
    public static void main(String[] args) {
        Object lock = new Object();
        CycleIterator firstIt = new CycleIterator(1, 10);
        CycleIterator secondIt = new CycleIterator(1, 10);
        NumbersPrinterThread firstPrinter = new NumbersPrinterThread("first", lock, false, firstIt);
        NumbersPrinterThread secondPrinter = new NumbersPrinterThread("second", lock, true, secondIt);
        firstPrinter.start();
        secondPrinter.start();
    }
}
