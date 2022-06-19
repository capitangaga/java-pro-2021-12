package ru.kirillgolovko.cw.common.model;

/**
 * @author kirillgolovko
 */
public class GameFieldState {
    public static final GameFieldState DEFAULT_STATE = new GameFieldState(new Point(0.001, 0.001), 0.5, 0.5, 0, 0);

    private final Point ballPosition;
    private final double leftHandle;
    private final double rightHandle;
    private final int leftScore;
    private final int rightScore;

    public GameFieldState(Point ballPosition, double leftHandle, double rightHandle, int leftScore, int rightScore) {
        this.ballPosition = ballPosition;
        this.leftHandle = leftHandle;
        this.rightHandle = rightHandle;
        this.leftScore = leftScore;
        this.rightScore = rightScore;
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
}
