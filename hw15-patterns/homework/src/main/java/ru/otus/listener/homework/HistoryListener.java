package ru.otus.listener.homework;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> storage = new HashMap<>();

    @Override
    public void onUpdated(Message msg) {
        Message copy = msg.copy();
        storage.put(copy.getId(), copy);
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return Optional.of(storage.get(id)).map(Message::copy);
    }
}
