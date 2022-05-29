package ru.kirillgolovko.otus.javapro.executors;

/**
 * @author kirillgolovko
 */
public class NumbersPrinterThread extends Thread {
    private final Iterable<Integer> numbersSequence;
    private final FlipFlop flipFlop;

    public NumbersPrinterThread(
            String name,
            Iterable<Integer> numbersSequence,
            FlipFlop flipFlop)
    {
        super(name);
        this.numbersSequence = numbersSequence;
        this.flipFlop = flipFlop;
    }


    @Override
    public void run() {
        try {
            for (int number : numbersSequence) {
                synchronized (flipFlop) {
                    while (!flipFlop.isMyTurn(getName())) {
                        flipFlop.wait();
                    }
                    System.out.printf("Thread %s - %d%n", super.getName(), number);
                    flipFlop.flip();
                    flipFlop.notifyAll();
                }

            }

        } catch (InterruptedException interruption) {
            System.out.println(Thread.currentThread().getName() + " - time to die");
        }
    }
}
