package ru.otus.processor.homework;

import java.time.Instant;
import java.util.List;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import ru.otus.handler.ComplexProcessor;
import ru.otus.model.Message;

class ExceptionThrowingProcessorTest {
    private final Message message = new Message.Builder(123).build();

    @Test
    public void testEven() {
       doTestForTs(Instant.ofEpochSecond(8), Mockito.only());
    }

    @Test
    public void testOdd() {
        doTestForTs(Instant.ofEpochSecond(9), Mockito.never());
    }

    private void doTestForTs(Instant ts, VerificationMode exceptionConsumerCallVerefication) {
        ExceptionThrowingProcessor processor = new ExceptionThrowingProcessor(() -> ts);
        Consumer spy = Mockito.spy(Consumer.class);
        ComplexProcessor complexProcessor = new ComplexProcessor(List.of(processor), spy);
        complexProcessor.handle(message);
        Mockito.verify(spy, exceptionConsumerCallVerefication).accept(Mockito.any());
    }
}