package ru.kirillgolovko.otus.javapro.atm.model;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Map;

/**
 * @author kirillgolovko
 */
public class PutResult {
    private final BigDecimal amount;
    private final Map<Nominal, Integer> badBanknotes;

    public PutResult(BigDecimal amount, Map<Nominal, Integer> badBanknotes) {
        this.amount = amount;
        this.badBanknotes = badBanknotes;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Map<Nominal, Integer> getBadBanknotes() {
        return Collections.unmodifiableMap(badBanknotes);
    }
}
