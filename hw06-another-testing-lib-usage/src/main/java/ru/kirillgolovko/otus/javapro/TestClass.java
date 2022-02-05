package ru.kirillgolovko.otus.javapro;

import ru.kirillgolovko.otus.javapro.atl.annotations.After;
import ru.kirillgolovko.otus.javapro.atl.annotations.Before;
import ru.kirillgolovko.otus.javapro.atl.annotations.Disabled;
import ru.kirillgolovko.otus.javapro.atl.annotations.Test;

public class TestClass {
    @Before
    public void doBefore() {
        System.out.println("Before");
    }

    @Test
    public void test1() {
        System.out.println("Test 1");
    }

    @Test(name = "Тут тестим второй тест")
    public void test2() {
        System.out.println("Test 2");
    }

    @Test(name = "A этот сломаем")
    public void testBroken() {
        throw new RuntimeException("Я сломалсо");
    }

    @Test(name = "A этот выключим")
    @Disabled
    public void testDisabled() {
        throw new RuntimeException("Я сломалсо");
    }

    @After
    public void doAfter() {
        System.out.println("After");
    }
}
