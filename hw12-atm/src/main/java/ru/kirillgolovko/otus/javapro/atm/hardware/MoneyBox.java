package ru.kirillgolovko.otus.javapro.atm.hardware;


import java.util.List;

import ru.kirillgolovko.otus.javapro.atm.model.Nominal;

/**
 * @author kirillgolovko
 */
public class MoneyBox {
    private final Nominal nominal;
    private int count;

    private MoneyBox(Nominal nominal, int count) {
        this.nominal = nominal;
        this.count = count;
    }

    public static List<MoneyBox> getSupportedBoxes(int count, List<Nominal> supportedNominal) {
        return supportedNominal.stream().map(nominal -> new MoneyBox(nominal, count)).toList();
    }

    public boolean getBanknotes(int count) {
        if (count <= this.count) {
            this.count -= count;
            return true;
        }
        return false;
    }

    public void putBanknotes(int count) {
        this.count += count;
    }

    public Nominal getNominal() {
        return nominal;
    }

    public int getCount() {
        return count;
    }
}
