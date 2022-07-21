package ru.kirillgolovko.otus.cw.gameserver.session;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * @author kirillgolovko
 */
public class GameSessionsService {

    private final SimpMessagingTemplate simp;

    private final Map<String, GameSession> gameSessions = Collections.synchronizedMap(new HashMap<>());

    public GameSessionsService(SimpMessagingTemplate simp) {
        this.simp = simp;
    }


    public GameSession getOrCreateSession(String id) {
        return gameSessions.computeIfAbsent(id, (sessionId) -> new GameSession(simp, sessionId));
    }

    public Optional<GameSession> getSession(String id) {
        return Optional.ofNullable(gameSessions.get(id));
    }

    @Scheduled(fixedRate = 5000)
    public void cleanup() {
        Set<String> toRemove = new HashSet<>();
        gameSessions.forEach((key, value) -> {
            if (value.isFinished()) {
                toRemove.add(key);
            }
        });
        toRemove.forEach(gameSessions::remove);
    }
}
