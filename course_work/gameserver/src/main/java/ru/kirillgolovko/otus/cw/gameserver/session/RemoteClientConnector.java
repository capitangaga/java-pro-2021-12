package ru.kirillgolovko.otus.cw.gameserver.session;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.logging.Logger;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import ru.kirillgolovko.cw.common.game.client.GameClient;
import ru.kirillgolovko.cw.common.model.game.GameFieldState;
import ru.kirillgolovko.cw.common.model.game.KeyboardEvent;

/**
 * @author kirillgolovko
 */
public class RemoteClientConnector extends Thread implements GameClient {
    private static final Logger logger = Logger.getLogger(RemoteClientConnector.class.getName());
    private final List<Consumer<KeyboardEvent>> consumers = new ArrayList<>(1);
    private final BlockingQueue<GameFieldState> statesQueue = new LinkedBlockingQueue<>(100);

    private final String stateTopic;
    private final SimpMessagingTemplate simp;

    public RemoteClientConnector(String stateTopic, SimpMessagingTemplate simp) {
        this.stateTopic = stateTopic;
        this.simp = simp;
    }

    @Override
    public void startGame() {
        this.start();
    }

    @Override
    public void stopGame() {
        this.interrupt();
    }

    @Override
    public void addKeyboardEventsConsumer(Consumer<KeyboardEvent> consumer) {
        consumers.add(consumer);
    }

    @Override
    public void removeKeyboardEventsConsumer(Consumer<KeyboardEvent> consumer) {
        consumers.remove(consumer);
    }

    @Override
    public boolean updateFieldState(GameFieldState gameFieldState) {
        return statesQueue.add(gameFieldState);
    }

    @Override
    public void run() {
        logger.info("Started sender" + stateTopic);
        while (!this.isInterrupted()) {
            try {
                GameFieldState nextState = statesQueue.take();
                simp.convertAndSend(stateTopic, nextState);
            } catch (InterruptedException e) {
                break;
            }
        }
    }

    public void nextKeyboardEvent(KeyboardEvent event) {
        for (var consumer : consumers) {
            consumer.accept(event);
        }
    }
}