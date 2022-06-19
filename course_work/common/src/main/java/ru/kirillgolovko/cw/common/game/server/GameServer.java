package ru.kirillgolovko.cw.common.game.server;

import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.kirillgolovko.cw.common.Utils;
import ru.kirillgolovko.cw.common.game.client.GameClient;
import ru.kirillgolovko.cw.common.model.GameFieldState;
import ru.kirillgolovko.cw.common.model.KeyboardEvent;
import ru.kirillgolovko.cw.common.model.KeyboardEventType;
import ru.kirillgolovko.cw.common.model.Point;

/**
 * @author kirillgolovko
 */
public class GameServer extends Thread {

    private final GameClient leftClient;
    private final GameClient rightClient;
    private final GameServerSettings gameServerSettings;

    private ConcurrentLinkedQueue<KeyboardEvent> leftClientEvents;
    private ConcurrentLinkedQueue<KeyboardEvent> rightClientEvents;

    // field state here
    private Point ballPosition = new Point(.5, .5);
    private Point ballVector;

    private double leftHandlePos = 0.5;
    private double rightHandlePos = 0.5;

    private long lastCall;



    public GameServer(
            GameClient leftClient,
            GameClient rightClient,
            String sessionId,
            GameServerSettings gameServerSettings)
    {
        super(sessionId);
        this.leftClient = leftClient;
        this.rightClient = rightClient;
        this.gameServerSettings = gameServerSettings;
        leftClientEvents = new ConcurrentLinkedQueue<>();
        rightClientEvents = new ConcurrentLinkedQueue<>();

    }

    public void init() {
        leftClient.addKeyboardEventsConsumer(event -> leftClientEvents.add(event));
        rightClient.addKeyboardEventsConsumer(event -> rightClientEvents.add(event));
    }

    private double calcNewHandlePosition(ConcurrentLinkedQueue<KeyboardEvent> queue, double previousPosition) {
        List<KeyboardEvent> keyboardEvents = Utils.readAllFromQueueLimited(queue, 1000);
        if (keyboardEvents.isEmpty()) {
            return previousPosition;
        }
        // отматываем чуть назад, даем zeroEventOffset мс на потупить laternе
        long prevEventTs = keyboardEvents.get(0).getMillis() - gameServerSettings.zeroEventOffset;
        double delta = 0;
        for(var keyboardEvent : keyboardEvents) {
             delta += (keyboardEvent.getEventType().equals(KeyboardEventType.ARROW_UP) ? 1 : -1)
                    * (keyboardEvent.getMillis() - prevEventTs)
                    * gameServerSettings.handleSpeed;
            prevEventTs = keyboardEvent.getMillis();
        }
        System.out.println(delta);
        previousPosition = Math.max(0, Math.min(previousPosition + delta, 1));
        return previousPosition;
    }

    private void moveBall() {

    }

    @Override
    public void run() {
        lastCall = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            leftHandlePos = calcNewHandlePosition(leftClientEvents, leftHandlePos);
            rightHandlePos = calcNewHandlePosition(rightClientEvents, rightHandlePos);


            GameFieldState nextState = new GameFieldState(ballPosition, leftHandlePos, rightHandlePos, 0, 0);
            leftClient.updateFieldState(nextState);
            rightClient.updateFieldState(nextState);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
