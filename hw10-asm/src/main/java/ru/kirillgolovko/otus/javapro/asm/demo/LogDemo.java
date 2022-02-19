package ru.kirillgolovko.otus.javapro.asm.demo;

import java.util.Arrays;
import java.util.List;

import ru.kirillgolovko.otus.javapro.asm.annotation.Log;

/**
 * @author kirillgolovko
 */
public class LogDemo {
    @Log
    public static String sum(String a, String b, int c) {
        return a + b + c;
    }

    public static int sum(int a, int b) {
        return a + b;
    }

    @Log
    public void bigMethodManyTypes(int a, String b, int[] c, String[] d, Long e, boolean f, List<Integer> g) {
        // do something...
        int wiredHash = a + b.hashCode() + Arrays.hashCode(c) + Arrays.hashCode(d) + e.hashCode()
                + Boolean.hashCode(f) + g.hashCode();
        System.out.println("Wired hash:" + wiredHash);
    }
}
