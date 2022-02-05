package ru.kirillgolovko.otus.javapro.atl.core.result;

public class TestResult {
    public final String testName;
    public final TestVerdict verdict;
    public final Exception exception;

    public TestResult(String testName, TestVerdict verdict, Exception exception) {
        this.testName = testName;
        this.verdict = verdict;
        this.exception = exception;
    }
}
