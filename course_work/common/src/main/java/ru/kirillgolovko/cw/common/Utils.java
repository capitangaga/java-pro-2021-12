package ru.kirillgolovko.cw.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;


/**
 * @author kirillgolovko
 */
public class Utils {
    public static <T> List<T> readAllFromQueueLimited(Queue<T> queue, int limit) {
        T next;
        List<T> result = new ArrayList<>();
        int c = 0;
        while ((next = queue.poll()) != null && c < limit) {
            result.add(next);
            c++;
        }
        return result;
    }
}
