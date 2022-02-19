package ru.kirillgolovko.otus.javapro.atm;

import java.math.BigDecimal;
import java.util.Map;

import ru.kirillgolovko.otus.javapro.atm.bank.BankApi;
import ru.kirillgolovko.otus.javapro.atm.hardware.MoneyStorage;
import ru.kirillgolovko.otus.javapro.atm.model.GetResult;
import ru.kirillgolovko.otus.javapro.atm.model.Nominal;
import ru.kirillgolovko.otus.javapro.atm.model.PutResult;

/**
 * @author kirillgolovko
 */
public class Atm {
    private final BankApi bankApi;
    private final MoneyStorage moneyStorage;

    private Atm(BankApi bankApi, MoneyStorage moneyStorage) {
        this.bankApi = bankApi;
        this.moneyStorage = moneyStorage;
    }

    public static Atm getDefaultInstance() {
        return new Atm(BankApi.getEmpty(), MoneyStorage.getStorageWithDefaultAmount());
    }

    BigDecimal getAmount(int uid) {
        return bankApi.getMoney(uid);
    }

    GetResult getMoney(int uid, BigDecimal amount) {
        if (bankApi.getMoney(uid).compareTo(amount) >= 0) {
            GetResult atmHardwareResult = moneyStorage.getMoney(amount);
            if (atmHardwareResult.isSucceed()) {
                bankApi.applyTransaction(uid, amount.negate());
            }
            return atmHardwareResult;
        }
        return GetResult.failed();
    }

    PutResult putMoney(int uid, Map<Nominal, Integer> banknotes) {
        PutResult putResult = moneyStorage.putMoney(banknotes);
        bankApi.applyTransaction(uid, putResult.getAmount());
        return putResult;
    }
}
