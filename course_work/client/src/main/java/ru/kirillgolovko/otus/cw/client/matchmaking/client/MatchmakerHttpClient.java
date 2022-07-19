package ru.kirillgolovko.otus.cw.client.matchmaking.client;

import java.util.Optional;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;

import ru.kirillgolovko.cw.common.model.matchmaking.MatchmakingResult;

/**
 * @author kirillgolovko
 */
public class MatchmakerHttpClient implements MatchmakerClient {
    private final String baseUrl;
    private final int port;

    private Client client = ClientBuilder.newClient();

    public MatchmakerHttpClient(String baseUrl, int port) {
        this.baseUrl = baseUrl;
        this.port = port;
    }

    @Override
    public Optional<MatchmakingResult> getSession(String username) {
        String URI = String.format("%s:%d/find-game", baseUrl, port);
        try {
            MatchmakingResult match
                    = client.target(URI).path(username).request(MediaType.APPLICATION_JSON).get(MatchmakingResult.class);
            System.out.printf("MM response: %s\n", match);
            if (match.getMatch() != null && match.getGameHost() != null) {
                return Optional.of(match);
            } return Optional.empty();
        } catch (RuntimeException ex) {
            System.out.println(ex);
            return Optional.empty();
        }
    }
}
