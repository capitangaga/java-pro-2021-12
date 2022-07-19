package ru.kirillgolovko.otus.cw.client.matchmaking.client;

import java.util.Optional;

import ru.kirillgolovko.cw.common.model.matchmaking.MatchmakingResult;

/**
 * @author kirillgolovko
 */
public interface MatchmakerClient {
    Optional<MatchmakingResult> getSession(String username);
}
