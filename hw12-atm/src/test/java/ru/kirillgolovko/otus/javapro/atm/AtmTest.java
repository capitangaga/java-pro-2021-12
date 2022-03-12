package ru.kirillgolovko.otus.javapro.atm;


import java.math.BigDecimal;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.kirillgolovko.otus.javapro.atm.model.GetResult;
import ru.kirillgolovko.otus.javapro.atm.model.Nominal;
import ru.kirillgolovko.otus.javapro.atm.model.PutResult;

class AtmTest {
    private final int myUser1 = 10;
    private final int myUser2 = 20;

    private final Map<Nominal, Integer> put1 = Map.of(Nominal.R100, 10, Nominal.R500, 2);
    private final Map<Nominal, Integer> put2 = Map.of(Nominal.R100, 10, Nominal.R50, 1);

    private final Atm atm = Atm.getDefaultInstance();

    @Test
    public void getAmount() {
        BigDecimal amount = atm.getAmount(myUser1);
        Assertions.assertEquals(BigDecimal.ZERO, amount);
    }

    @Test
    public void putMoney() {
        PutResult putResult = atm.putMoney(myUser1, put1);
        Assertions.assertTrue(putResult.getBadBanknotes().isEmpty());
        Assertions.assertEquals(BigDecimal.valueOf(2000), putResult.getAmount());

        PutResult putResult2 = atm.putMoney(myUser2, put2);
        Assertions.assertEquals(Map.of(Nominal.R50, 1), putResult2.getBadBanknotes());
        Assertions.assertEquals(BigDecimal.valueOf(1000), putResult2.getAmount());

        BigDecimal get1 = atm.getAmount(myUser1);
        Assertions.assertEquals(BigDecimal.valueOf(2000), get1);

        BigDecimal get2 = atm.getAmount(myUser2);
        Assertions.assertEquals(BigDecimal.valueOf(1000), get2);
    }

    @Test
    public void getMoney() {
        atm.putMoney(myUser1, put1);
        atm.putMoney(myUser2, put2);
        GetResult money = atm.getMoney(myUser1, BigDecimal.valueOf(1900));
        Assertions.assertTrue(money.isSucceed());
        Assertions.assertEquals(Map.of(Nominal.R1000, 1, Nominal.R500, 1, Nominal.R200, 2), money.getBanknotes());

        BigDecimal amount = atm.getAmount(myUser1);
        Assertions.assertEquals(BigDecimal.valueOf(100), amount);

        BigDecimal amount2 = atm.getAmount(myUser2);
        Assertions.assertEquals(BigDecimal.valueOf(1000), amount2);
    }

    @Test
    public void getToMuch() {
        GetResult money = atm.getMoney(myUser1, BigDecimal.TEN);
        Assertions.assertFalse(money.isSucceed());
    }

    @Test
    public void noSuchBanknotes() {
        atm.putMoney(myUser1, put1);
        GetResult money = atm.getMoney(myUser1, BigDecimal.ONE);
        Assertions.assertFalse(money.isSucceed());

        BigDecimal amount = atm.getAmount(myUser1);
        Assertions.assertEquals(BigDecimal.valueOf(2000), amount);
    }
}