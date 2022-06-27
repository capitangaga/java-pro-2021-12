package ru.kirillgolovko.otus.cw.matchmaker;

import org.springframework.stereotype.Service;
import ru.kirillgolovko.cw.common.model.matchmaking.Match;

import java.util.*;

@Service
public class SimpleMatchmaker implements Matchmaker {
    private final Random r = new Random();

    private String waitingUser;
    private final Map<String, Match> madeMatches = new HashMap<>();

    @Override
    // non-scalable simple impl
    public synchronized Optional<Match> getMatchForUser(String username) {
        if (madeMatches.containsKey(username)) {
            return Optional.of(madeMatches.remove(username));
        }

        if (waitingUser != null && waitingUser.equals(username)) {
            return Optional.empty();
        }

        if (waitingUser != null) {
            String sessionId = r.nextLong(Long.MAX_VALUE) + "_" + System.currentTimeMillis();
            madeMatches.put(waitingUser, new Match(sessionId, "r"));
            waitingUser = null;
            return Optional.of(new Match(sessionId, "l"));
        }

        waitingUser = username;
        return Optional.empty();

    }
}
