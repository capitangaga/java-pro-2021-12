package ru.kirillgolovko.otus.javapro.atl.core;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import ru.kirillgolovko.otus.javapro.atl.core.result.TestResult;
import ru.kirillgolovko.otus.javapro.atl.core.result.TestVerdict;

public class RealTestRunner implements TestRunner{
    private static final Logger logger = LogManager.getLogger(RealTestRunner.class);

    private final String testName;
    private final Runnable before;
    private final Runnable test;
    private final Runnable after;

    public RealTestRunner(String testName, Runnable before, Runnable test, Runnable after) {
        this.testName = testName;
        this.before = before;
        this.test = test;
        this.after = after;
    }

    @Override
    public TestResult executeTest() {
        logger.debug("Preparing test {}", testName);
        Optional<Exception> beforeEx = runSafeAndCheckNoExceptions(before);
        if (beforeEx.isPresent()) {
            return finalizeExceptionally(false, beforeEx.get());
        }
        logger.debug("Prepared, running test {}", testName);
        Optional<Exception> testEx = runSafeAndCheckNoExceptions(test);
        if (testEx.isPresent()) {
            return finalizeExceptionally(true, testEx.get());
        }
        logger.debug("Finalizing {} test case after successful test evaluation", testName);
        Optional<Exception> finalEx = runSafeAndCheckNoExceptions(after);
        if (finalEx.isPresent()) {
            logger.debug("Error finalizing test case {}, ex {}", testName, finalEx.get());
            return new TestResult(testName, TestVerdict.ERROR, finalEx.get());
        }
        logger.debug("Done test case {}", testName);
        return new TestResult(testName, TestVerdict.OK, null);
    }

    private TestResult finalizeExceptionally(boolean failInTestBody, Exception exception) {
        logger.debug("Test case {} stage failed, trying to finalize", testName);
        // better we try to do all finalization stuff before fail
        Optional<Exception> finalizationEx = runSafeAndCheckNoExceptions(() -> {
            after.run();
            logger.debug("Test case {} emergency finalization done", testName);
        });
        finalizationEx.ifPresent(e -> logger.debug("Error finalizing test case {}, ex {}", testName, e));
        if (failInTestBody) {
            return new TestResult(testName, TestVerdict.FAILED, exception);
        }
        return new TestResult(testName, TestVerdict.ERROR, exception);
    }

    private static Optional<Exception> runSafeAndCheckNoExceptions(Runnable runnable) {
        try {
            runnable.run();
            logger.debug("Runnable complete");
            return Optional.empty();
        } catch (Exception ex) {
            logger.debug("Exception while runnable execution", ex);
            return Optional.of(ex);
        }
    }
}
