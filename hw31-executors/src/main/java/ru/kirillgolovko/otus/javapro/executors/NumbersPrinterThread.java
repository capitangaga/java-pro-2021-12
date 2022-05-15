package ru.kirillgolovko.otus.javapro.executors;

/**
 * @author kirillgolovko
 */
public class NumbersPrinterThread extends Thread {
    private final Object lock;
    private final boolean slave;
    private final Iterable<Integer> numbersSequence;

    public NumbersPrinterThread(String name, Object lock, boolean slave, Iterable<Integer> numbersSequence) {
        super(name);
        this.lock = lock;
        this.slave = slave;
        this.numbersSequence = numbersSequence;
    }


    @Override
    public void run() {
        try {
            // начинает кто-то один
            if (slave) {
                synchronized (lock) {
                    lock.wait();
                }
            }

            for (int number : numbersSequence) {
                System.out.printf("Thread %s - %d%n", super.getName(), number);
                synchronized (lock) {
                    lock.notify();
                    lock.wait();
                }
            }

        } catch (InterruptedException interruption) {
            System.out.println(Thread.currentThread().getName() + " - time to die");
        }
    }
}
