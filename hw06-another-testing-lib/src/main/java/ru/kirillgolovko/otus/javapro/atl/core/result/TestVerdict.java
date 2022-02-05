package ru.kirillgolovko.otus.javapro.atl.core.result;

public enum TestVerdict {
    /**
     * Everything is fine
     */
    OK,
    /**
     * Exception during test execution
     */
    FAILED,
    /**
     * Exception during test setup or finalization
     */
    ERROR,
    /**
     * Test marked with @Disabled annotation
     */
    DISABLED
}
