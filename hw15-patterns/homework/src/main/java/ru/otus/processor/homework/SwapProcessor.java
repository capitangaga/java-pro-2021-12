package ru.otus.processor.homework;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

/**
 * @author kirillgolovko
 */
public class SwapProcessor implements Processor {

    @Override
    public Message process(Message message) {
        Message.Builder builder = message.toBuilder();
        builder.field11(message.getField12());
        builder.field12(message.getField11());
        return builder.build();
    }
}
