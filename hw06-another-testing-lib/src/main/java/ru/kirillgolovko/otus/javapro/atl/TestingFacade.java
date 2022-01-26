package ru.kirillgolovko.otus.javapro.atl;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kirillgolovko.otus.javapro.atl.core.result.ClassProcessor;
import ru.kirillgolovko.otus.javapro.atl.core.result.TestResult;
import ru.kirillgolovko.otus.javapro.atl.core.result.TestVerdict;

public class TestingFacade {
    private static final Logger logger = LogManager.getLogger(TestingFacade.class);

    public static List<TestResult> runTestsParallel(String className, int numberOfThreads) {
        try {
            Class<?> classToTest = Class.forName(className);
            return runTestsParallel(classToTest, numberOfThreads);
        } catch (ClassNotFoundException e) {
            logger.error("Not found class for name {}", className);
            throw new IllegalArgumentException(String.format("No class for name %s", className));
        }
    }

    public static List<TestResult> runTests(String className) {
        try {
            Class<?> classToTest = Class.forName(className);
            return runTests(classToTest);
        } catch (ClassNotFoundException e) {
            logger.error("Not found class for name {}", className);
            throw new IllegalArgumentException(String.format("No class for name %s", className));
        }
    }


    public static List<TestResult> runTestsParallel(Class<?> clazz, int numberOfThreads) {
        List<Supplier<TestResult>> testResultSuppliers = ClassProcessor.getTestResultSuppliers(clazz);
        logger.info("Got {} tests for {} class", testResultSuppliers.size(), clazz.getCanonicalName());
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        List<TestResult> testResults = testResultSuppliers
                .stream()
                .map(getter -> executorService.submit(getter::get))
                .map(future -> {
                    try {
                        return future.get();
                    } catch (Exception ex) {
                        throw new RuntimeException("Failed awaiting test", ex);
                    }
                }).toList();
        logger.info("Done testing, {} tests finished", testResults.size());
        executorService.shutdown();
        return testResults;
    }

    public static List<TestResult> runTests(Class<?> clazz) {
        List<Supplier<TestResult>> testResultSuppliers = ClassProcessor.getTestResultSuppliers(clazz);
        logger.info("Got {} tests for {} class", testResultSuppliers.size(), clazz.getCanonicalName());
        List<TestResult> testResults = testResultSuppliers.stream().map(Supplier::get).toList();
        logger.info("Done testing, {} tests finished", testResults.size());
        return testResults;
    }

    public static String buildReport(List<TestResult> testResults) {
        StringBuilder builder = new StringBuilder();
        builder.append("Total tests: ").append(testResults.size()).append("\n");
        for (var verdict : TestVerdict.values()) {
            builder
                    .append(verdict.toString()).append(": ")
                    .append(testResults.stream().filter(testResult -> testResult.verdict.equals(verdict)).count())
                    .append("\n");
        }
        if (testResults.stream().anyMatch(testResult -> testResult.verdict.equals(TestVerdict.FAILED))) {
            builder.append("\nFAILED TESTS:\n");
            testResults.stream()
                    .filter(testResult -> testResult.verdict.equals(TestVerdict.FAILED))
                    .forEach(testResult -> builder
                            .append(testResult.testName)
                            .append("\n")
                            .append(testResult.exception.getCause())
                            .append("\n"));
        }
        if (testResults.stream().anyMatch(testResult -> testResult.verdict.equals(TestVerdict.ERROR))) {
            builder.append("\nERROR TESTS:\n");
            testResults.stream()
                    .filter(testResult -> testResult.verdict.equals(TestVerdict.ERROR))
                    .forEach(testResult -> builder
                            .append(testResult.testName)
                            .append("\n")
                            .append(testResult.exception.getCause())
                            .append("\n"));
        }
        return builder.toString();
    }
}
