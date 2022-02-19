package ru.kirillgolovko.otus.javapro.asm.demo;

import java.util.List;

/**
 * Run with: java -javaagent:demo.jar -jar demo.jar
 * @author kirillgolovko
 */
public class DemoMain {
    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            String sum = LogDemo.sum("a", "b", i);
        }

        LogDemo logDemo = new LogDemo();
        logDemo.bigMethodManyTypes(10, "aa", new int[] {1, 2, 3}, new String[] {"a", "b"}, 100L, true, List.of(1, 2, 3));

        System.out.println("Should print nothing");
        int sum = LogDemo.sum(1, 2);
        System.out.println("-------------");

    }
}
