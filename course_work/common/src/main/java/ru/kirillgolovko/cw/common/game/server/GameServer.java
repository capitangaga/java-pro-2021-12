package ru.kirillgolovko.cw.common.game.server;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import ru.kirillgolovko.cw.common.MathUtils;
import ru.kirillgolovko.cw.common.Utils;
import ru.kirillgolovko.cw.common.game.client.GameClient;
import ru.kirillgolovko.cw.common.model.game.GameFieldState;
import ru.kirillgolovko.cw.common.model.game.KeyboardEvent;
import ru.kirillgolovko.cw.common.model.game.KeyboardEventType;
import ru.kirillgolovko.cw.common.model.game.Point;

/**
 * @author kirillgolovko
 */
public class GameServer extends Thread {
    private static final Random random = new Random();

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

    private int rightScore = 0;
    private int leftScore = 0;

    private long gameStatTs;

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
        ballVector = getInitialMovement();

    }

    public void init() {
        leftClient.addKeyboardEventsConsumer(event -> leftClientEvents.add(event));
        rightClient.addKeyboardEventsConsumer(event -> rightClientEvents.add(event));
        leftClient.startGame();
        rightClient.startGame();
        gameStatTs = System.currentTimeMillis();
    }

    public void shutdown() {
        leftClient.stopGame();
        rightClient.stopGame();
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

    private void moveBall(long lastCall, long now) {
        Point nextPoint = ballPosition.plus(ballVector.mult(now - lastCall));
        if (nextPoint.getX() < 0) {
            rightScore++;
            restartGame();
            return;
        }
        if (nextPoint.getX() > 1) {
            leftScore++;
            restartGame();
            return;
        }

        if (nextPoint.getY() < 0) {
            ballVector = MathUtils.mirrorVec(ballVector, nextPoint, new Point(nextPoint.getX(), 1));
            ballPosition = nextPoint.plus(ballVector.mult(now - lastCall));
            return;
        }

        if (nextPoint.getY() > 1) {
            ballVector = MathUtils.mirrorVec(ballVector, nextPoint, new Point(nextPoint.getX(), 0));
            ballPosition = nextPoint.plus(ballVector.mult(now - lastCall));
            return;
        }

        Point leftPadCenter = new Point(0, leftHandlePos);
        if (mirrorFromPad(lastCall, now, nextPoint, leftPadCenter)) {
            return;
        }

        Point rightPadCenter = new Point(1, rightHandlePos);
        if(mirrorFromPad(lastCall, now, nextPoint, rightPadCenter)) {
            return;
        }

        ballPosition = nextPoint;

    }

    private boolean mirrorFromPad(long lastCall, long now, Point nextPoint, Point rightPadCenter) {
        if (MathUtils.pointInCircle(rightPadCenter, gameServerSettings.padRadius, nextPoint)) {
            Point circleAndLineCrossing = MathUtils
                    .findCircleAndLineCrossing(rightPadCenter, gameServerSettings.padRadius, ballPosition, nextPoint);
            ballVector = MathUtils.mirrorVec(ballVector, rightPadCenter, circleAndLineCrossing);
            ballPosition = ballPosition.plus(ballVector.mult(now - lastCall));
            return true;
        }
        return false;
    }

    private void restartGame() {
        ballPosition = new Point(.5, .5);
        ballVector = getInitialMovement();
    }

    private Point getInitialMovement() {
        boolean dir = random.nextBoolean();
        return dir ? new Point(gameServerSettings.ballSpeed, 0) : new Point(-gameServerSettings.ballSpeed, 0);
    }

    @Override
    public void run() {
        long lastCall = System.currentTimeMillis();
        while (!Thread.currentThread().isInterrupted()) {
            leftHandlePos = calcNewHandlePosition(leftClientEvents, leftHandlePos);
            rightHandlePos = calcNewHandlePosition(rightClientEvents, rightHandlePos);
            long now = System.currentTimeMillis();
            moveBall(lastCall, now);
            lastCall = now;

            long remainingTime = gameServerSettings.matchLength - (now - gameStatTs);
            boolean gameIsOver = remainingTime < 0;

            GameFieldState nextState = new GameFieldState(
                    ballPosition,
                    leftHandlePos,
                    rightHandlePos,
                    leftScore,
                    rightScore,
                    remainingTime,
                    gameIsOver,
                    "");

            if (gameIsOver) {
                processGameOver(nextState);
                break;
            }

            leftClient.updateFieldState(nextState);
            rightClient.updateFieldState(nextState);
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void processGameOver(GameFieldState gameFieldState) {
        GameFieldState leftState = gameFieldState.withGameOverMessage(getGameOverMessage(leftScore, rightScore));
        leftClient.updateFieldState(leftState);
        GameFieldState rightState = gameFieldState.withGameOverMessage(getGameOverMessage(rightScore, leftScore));
        rightClient.updateFieldState(rightState);
    }

    private String getGameOverMessage(int thisScore, int otherScore) {
        String verdict;
        if (thisScore == otherScore) {
            verdict = "Draw.";
        } else if (thisScore < otherScore) {
            verdict = "You lost(";
        } else {
            verdict = "You won!";
        }
        return verdict + String.format("Your score: %s, opponent score: %s", thisScore, otherScore);
    }


}
