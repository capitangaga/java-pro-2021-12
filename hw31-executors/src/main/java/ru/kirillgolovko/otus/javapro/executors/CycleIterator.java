package ru.kirillgolovko.otus.javapro.executors;

import java.util.Iterator;

/**
 * @author kirillgolovko
 */
public class CycleIterator implements Iterator<Integer>, Iterable<Integer> {

    private final int min;
    private final int max;

    private int direction = -1;
    private int i;

    public CycleIterator(int min, int max) {
        assert min < max;
        this.min = min;
        this.max = max;
        this.i = min;
    }

    @Override
    public boolean hasNext() {
        return true;
    }

    @Override
    public Integer next() {
        if (i == max || i == min) {
            direction *= -1;
        }
        int result = i;
        i += direction;
        return result;
    }

    @Override
    public Iterator<Integer> iterator() {
        return this;
    }
}
