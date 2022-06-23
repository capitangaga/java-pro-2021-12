package ru.kirillgolovko.otus.cw.gameserver.session;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.kirillgolovko.otus.cw.gameserver.controller.HttpConnectionInterceptor;

/**
 * @author kirillgolovko
 */
@Configuration
public class GameSessionsServiceContextConfiguration {
    @Bean
    public GameSessionsService gameSessionsService(
            SimpMessagingTemplate simp,
            HttpConnectionInterceptor connectionInterceptor)
    {
        GameSessionsService gameSessionsService = new GameSessionsService(simp);
        connectionInterceptor.setGameSessionsService(gameSessionsService);
        return gameSessionsService;
    }
}
