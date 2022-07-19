package ru.kirillgolovko.cw.common.game.server;

/**
 * @author kirillgolovko
 */
public class GameServerSettings {
    public static final GameServerSettings DEFAULT_SETTINGS = new GameServerSettings(0.0005, 100, 0.0002, 0.075, 3 * 60000);

    public final double handleSpeed;
    public final int zeroEventOffset;
    public final double ballSpeed;
    public final double padRadius;
    public final long matchLength;

    public GameServerSettings(double handleSpeed, int zeroEventOffset, double ballSpeed, double padRadius, long matchLength) {
        this.handleSpeed = handleSpeed;
        this.zeroEventOffset = zeroEventOffset;
        this.ballSpeed = ballSpeed;
        this.padRadius = padRadius;
        this.matchLength = matchLength;
    }
}
