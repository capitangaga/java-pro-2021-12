package ru.kirillgolovko.cw.common.model.matchmaking;

public class Match {
    private final String sessionId;
    private final String side;

    public Match(String sessionId, String side) {
        this.sessionId = sessionId;
        this.side = side;
    }

    public Match() {
        this(null, null);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSide() {
        return side;
    }

    @Override
    public String toString() {
        return "Match{" +
                "sessionId='" + sessionId + '\'' +
                ", side='" + side + '\'' +
                '}';
    }
}
