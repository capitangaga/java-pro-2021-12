package ru.otus.processor.homework;

import java.time.Instant;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * @author kirillgolovko
 */
public class ExceptionThrowingProcessor implements Processor {

    private final TimeProvider timeProvider;

    public ExceptionThrowingProcessor(TimeProvider timeProvider) {
        this.timeProvider = timeProvider;
    }

    @Override
    public Message process(Message message) {
        Instant currentTs = timeProvider.getTime();
        if (currentTs.getEpochSecond() % 2 == 0) {
            throw new TimeProcessorException(currentTs);
        }
        return message;
    }

    public static class TimeProcessorException extends RuntimeException {
        private final Instant exceptionalTs;

        public TimeProcessorException(Instant exceptionalTs) {
            this.exceptionalTs = exceptionalTs;
        }

        public Instant getExceptionalTs() {
            return exceptionalTs;
        }
    }
}
