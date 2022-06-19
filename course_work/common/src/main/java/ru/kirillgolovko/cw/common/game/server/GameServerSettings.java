package ru.kirillgolovko.cw.common.game.server;

/**
 * @author kirillgolovko
 */
public class GameServerSettings {
    public static final GameServerSettings DEFAULT_SETTINGS = new GameServerSettings(0.001, 100);

    public final double handleSpeed;
    public final int zeroEventOffset;

    public GameServerSettings(double handleSpeed, int zeroEventOffset) {
        this.handleSpeed = handleSpeed;
        this.zeroEventOffset = zeroEventOffset;
    }
}
