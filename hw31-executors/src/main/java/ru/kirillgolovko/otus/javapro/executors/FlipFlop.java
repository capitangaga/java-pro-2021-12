package ru.kirillgolovko.otus.javapro.executors;

/**
 * @author kirillgolovko
 */
public class FlipFlop {
    private final String firstId;
    private final String secondId;

    private int state = 0;

    public FlipFlop(String firstId, String secondId) {
        assert firstId != null;
        assert secondId != null;
        assert !firstId.equals(secondId);
        this.firstId = firstId;
        this.secondId = secondId;

    }

    void flip() {
        state = (state + 1) % 2;
    }

    boolean isMyTurn(String id) {
        return state % 2 == 0 && firstId.equals(id) || state % 2 == 1 && secondId.equals(id);
    }
}
