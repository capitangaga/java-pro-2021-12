package ru.kirillgolovko.otus.javapro;

import java.util.List;

import ru.kirillgolovko.otus.javapro.atl.TestingFacade;
import ru.kirillgolovko.otus.javapro.atl.core.result.TestResult;

public class TestMain {

    public static void main(String[] args) {
        List<TestResult> testResults = TestingFacade.runTestsParallel("ru.kirillgolovko.otus.javapro.TestClass", 2);
        String report = TestingFacade.buildReport(testResults);
        System.out.println(report);
    }
}
