package ru.kirillgolovko.otus.cw.gameserver.controller;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;
import ru.kirillgolovko.cw.common.model.KeyboardEvent;
import ru.kirillgolovko.otus.cw.gameserver.session.GameSessionsService;

/**
 * @author kirillgolovko
 */
@Controller
public class GameServerController {
    private final GameSessionsService gameSessionsService;

    public GameServerController(GameSessionsService gameSessionsService) {
        this.gameSessionsService = gameSessionsService;
    }

    @MessageMapping("/keyboard_events/{session}/{side}")
    public void getKeyboardMessage(
            @DestinationVariable String session,
            @DestinationVariable String side,
            @Payload KeyboardEvent keyboardEvent)
    {
        gameSessionsService.getSession(session).ifPresent(gameSession -> gameSession.acceptEvent(side, keyboardEvent));
    }

}
