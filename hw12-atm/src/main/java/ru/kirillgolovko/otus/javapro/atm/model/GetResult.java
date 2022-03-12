package ru.kirillgolovko.otus.javapro.atm.model;

import java.util.Collections;
import java.util.Map;

import ru.kirillgolovko.otus.javapro.atm.hardware.MoneyStorage;

/**
 * @author kirillgolovko
 */
public class GetResult {
    private final boolean succeed;
    private final Map<Nominal, Integer> banknotes;

    private GetResult(boolean succeed, Map<Nominal, Integer> banknotes) {
        this.succeed = succeed;
        this.banknotes = banknotes;
    }

    public static GetResult failed() {
        return new GetResult(false, Collections.emptyMap());
    }

    public static GetResult ok(Map<Nominal, Integer> banknotes) {
        return new GetResult(true, banknotes);
    }

    public boolean isSucceed() {
        return succeed;
    }

    public Map<Nominal, Integer> getBanknotes() {
        return Collections.unmodifiableMap(banknotes);
    }
}
