package ru.kirillgolovko.cw.common.model.matchmaking;

public class Match {
    private final String sessionId;
    private final String side;
    private final String opponentName;

    public Match(String sessionId, String side, String opponentName) {
        this.sessionId = sessionId;
        this.side = side;
        this.opponentName = opponentName;
    }

    public Match() {
        this(null, null, null);
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getSide() {
        return side;
    }

    public String getOpponentName() {
        return opponentName;
    }

    @Override
    public String toString() {
        return "Match{" +
                "sessionId='" + sessionId + '\'' +
                ", side='" + side + '\'' +
                ", opponentName='" + opponentName + '\'' +
                '}';
    }
}
