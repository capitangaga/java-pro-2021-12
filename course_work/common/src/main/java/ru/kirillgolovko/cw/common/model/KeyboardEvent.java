package ru.kirillgolovko.cw.common.model;

/**
 * @author kirillgolovko
 */
public class KeyboardEvent {
    private final long millis;
    private final KeyboardEventType eventType;

    public KeyboardEvent(long millis, KeyboardEventType eventType) {
        this.millis = millis;
        this.eventType = eventType;
    }

    public long getMillis() {
        return millis;
    }

    public KeyboardEventType getEventType() {
        return eventType;
    }

    @Override
    public String toString() {
        return "KeyboardEvent{" +
                "millis=" + millis +
                ", eventType=" + eventType +
                '}';
    }
}
