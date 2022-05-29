package ru.kirillgolovko.otus.javapro.executors;

/**
 * @author kirillgolovko
 */
public class TwoFlipFlopPrintingThreads {
    private final NumbersPrinterThread first;
    private final NumbersPrinterThread second;

    private TwoFlipFlopPrintingThreads(NumbersPrinterThread first, NumbersPrinterThread second) {
        this.first = first;
        this.second = second;
    }

    void start() {
        first.start();
        second.start();
    }

    public static TwoFlipFlopPrintingThreads getInstance(String firstId, String secondId, int start, int stop) {
        FlipFlop flipFlop = new FlipFlop(firstId, secondId);
        return new TwoFlipFlopPrintingThreads(
                new NumbersPrinterThread(firstId, new CycleIterator(start, stop), flipFlop),
                new NumbersPrinterThread(secondId, new CycleIterator(start, stop), flipFlop));
    }
}
