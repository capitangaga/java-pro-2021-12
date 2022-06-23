package ru.kirillgolovko.cw.common.game.server;

/**
 * @author kirillgolovko
 */
public class GameServerSettings {
    public static final GameServerSettings DEFAULT_SETTINGS = new GameServerSettings(0.0005, 100, 0.0001, 0.1);

    public final double handleSpeed;
    public final int zeroEventOffset;
    public final double ballSpeed;
    public final double padRadius;

    public GameServerSettings(double handleSpeed, int zeroEventOffset, double ballSpeed, double padRadius) {
        this.handleSpeed = handleSpeed;
        this.zeroEventOffset = zeroEventOffset;
        this.ballSpeed = ballSpeed;
        this.padRadius = padRadius;
    }
}
