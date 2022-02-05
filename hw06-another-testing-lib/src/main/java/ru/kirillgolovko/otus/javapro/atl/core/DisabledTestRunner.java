package ru.kirillgolovko.otus.javapro.atl.core;

import ru.kirillgolovko.otus.javapro.atl.core.result.TestResult;
import ru.kirillgolovko.otus.javapro.atl.core.result.TestVerdict;

public class DisabledTestRunner implements TestRunner{

    private final String testName;

    public DisabledTestRunner(String testName) {
        this.testName = testName;
    }

    @Override
    public TestResult executeTest() {
        return new TestResult(testName, TestVerdict.DISABLED, null);
    }
}
