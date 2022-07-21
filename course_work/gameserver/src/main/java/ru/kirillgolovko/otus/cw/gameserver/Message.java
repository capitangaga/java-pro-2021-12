package ru.kirillgolovko.otus.cw.gameserver;

/**
 * @author kirillgolovko
 */
public class Message {
        private String from;
        private String text;

    public Message(String from, String text) {
        this.from = from;
        this.text = text;
    }
    public Message() {}

    public String getFrom() {
        return from;
    }

    public String getText() {
        return text;
    }

    // getters and setters

    public static class OutputMessage extends Message {
        private String time;

        public OutputMessage(String from, String text, String time) {
            super(from, text);
            this.time = time;
        }
    }
}
