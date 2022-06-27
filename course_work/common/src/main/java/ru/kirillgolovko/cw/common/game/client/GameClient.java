package ru.kirillgolovko.cw.common.game.client;

import java.util.function.Consumer;

import ru.kirillgolovko.cw.common.model.game.GameFieldState;
import ru.kirillgolovko.cw.common.model.game.KeyboardEvent;

/**
 * @author kirillgolovko
 */
public interface GameClient {
    void startGame();

    void stopGame();

    void addKeyboardEventsConsumer(Consumer<KeyboardEvent> consumer);

    void removeKeyboardEventsConsumer(Consumer<KeyboardEvent> consumer);

    boolean updateFieldState(GameFieldState gameFieldState);
}
