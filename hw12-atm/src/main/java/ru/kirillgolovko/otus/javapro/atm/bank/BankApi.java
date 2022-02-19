package ru.kirillgolovko.otus.javapro.atm.bank;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * @author kirillgolovko
 */
public class BankApi {
    private final Map<Integer, BigDecimal> accounts;

    private BankApi(Map<Integer, BigDecimal> accounts) {
        this.accounts = accounts;
    }

    public static BankApi getEmpty() {
        return new BankApi(new HashMap<>());
    }

    public final BigDecimal getMoney(int accId) {
        return accounts.getOrDefault(accId, BigDecimal.ZERO);
    }

    public final void applyTransaction(int accId, BigDecimal delta) {
        accounts.merge(accId, delta, BigDecimal::add);
    }
}
