package ru.kirillgolovko.cw.common.model.matchmaking;

/**
 * @author kirillgolovko
 */
public class MatchmakingResult {
    private Match match;
    private String gameHost;

    public MatchmakingResult(Match match, String gameHost) {
        this.match = match;
        this.gameHost = gameHost;
    }

    public MatchmakingResult() {
        this(null, null);
    }

    public Match getMatch() {
        return match;
    }

    public String getGameHost() {
        return gameHost;
    }

    @Override
    public String toString() {
        return "MatchmakingResult{" +
                "match=" + match +
                ", gameHost='" + gameHost + '\'' +
                '}';
    }
}
