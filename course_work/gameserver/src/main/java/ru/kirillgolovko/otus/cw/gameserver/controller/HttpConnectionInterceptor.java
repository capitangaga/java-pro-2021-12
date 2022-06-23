package ru.kirillgolovko.otus.cw.gameserver.controller;

import java.util.Map;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;
import org.springframework.web.util.UriComponentsBuilder;
import ru.kirillgolovko.otus.cw.gameserver.session.GameSessionsService;

/**
 * @author kirillgolovko
 */
@Component
public class HttpConnectionInterceptor implements HandshakeInterceptor {

    private GameSessionsService gameSessionsService;

    @Override
    public boolean beforeHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Map<String, Object> attributes) throws Exception
    {
        MultiValueMap<String, String> params = UriComponentsBuilder.fromHttpRequest(request).build().getQueryParams();
        String sessionId = params.getFirst("game_session_id");
        String side = params.getFirst("side");

        if (sessionId == null || side == null) {
            System.out.println("No headers");
            return false;
        }
        if (gameSessionsService != null) {
            gameSessionsService.getOrCreateSession(sessionId).connectClient(side);
        }
        return true;
    }

    @Override
    public void afterHandshake(
            ServerHttpRequest request,
            ServerHttpResponse response,
            WebSocketHandler wsHandler,
            Exception exception)
    {

    }

    public void setGameSessionsService(GameSessionsService gameSessionsService) {
        this.gameSessionsService = gameSessionsService;
    }
}
