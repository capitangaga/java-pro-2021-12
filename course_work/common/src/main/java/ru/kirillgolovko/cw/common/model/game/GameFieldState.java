package ru.kirillgolovko.cw.common.model.game;

/**
 * @author kirillgolovko
 */
public class GameFieldState {
    public static final GameFieldState DEFAULT_STATE
            = new GameFieldState(new Point(0.001, 0.001), 0.5, 0.5, 0, 0, 100, false, "");

    private Point ballPosition;
    private double leftHandle;
    private double rightHandle;
    private int leftScore;
    private int rightScore;

    private long remainingTime;

    private boolean gameIsOver;

    private String gameOverMessage;

    public GameFieldState(
            Point ballPosition,
            double leftHandle,
            double rightHandle,
            int leftScore,
            int rightScore,
            long remaningTime,
            boolean gameIsOver,
            String gameOverMessage)
    {
        this.ballPosition = ballPosition;
        this.leftHandle = leftHandle;
        this.rightHandle = rightHandle;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
        this.remainingTime = remaningTime;
        this.gameIsOver = gameIsOver;
        this.gameOverMessage = gameOverMessage;
    }

    public GameFieldState() {

    }

    public Point getBallPosition() {
        return ballPosition;
    }

    public double getLeftHandle() {
        return leftHandle;
    }

    public double getRightHandle() {
        return rightHandle;
    }

    public int getLeftScore() {
        return leftScore;
    }

    public int getRightScore() {
        return rightScore;
    }

    public long getRemainingTime() {
        return remainingTime;
    }

    public boolean isGameIsOver() {
        return gameIsOver;
    }

    public String getGameOverMessage() {
        return gameOverMessage;
    }

    public GameFieldState withGameOverMessage(String gameOverMessage) {
        return new GameFieldState(
                ballPosition,
                leftHandle,
                rightHandle,
                leftScore,
                rightScore,
                remainingTime,
                gameIsOver,
                gameOverMessage);
    }

}
