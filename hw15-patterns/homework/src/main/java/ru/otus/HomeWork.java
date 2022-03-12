package ru.otus;

import java.util.List;

import ru.otus.handler.ComplexProcessor;
import ru.otus.listener.homework.HistoryListener;
import ru.otus.model.Message;
import ru.otus.processor.homework.SwapProcessor;

public class HomeWork {
    public static void main(String[] args) {
        ComplexProcessor complexProcessor = new ComplexProcessor(List.of(new SwapProcessor()), (ex) -> {});
        Message message = new Message.Builder(10).field11("a").field12("b").build();
        System.out.println(message);
        HistoryListener historyListener = new HistoryListener();
        complexProcessor.addListener(historyListener);
        complexProcessor.handle(message);
        System.out.println(historyListener.findMessageById(10));
        complexProcessor.removeListener(historyListener);
    }
}
