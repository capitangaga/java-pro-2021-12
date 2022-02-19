package ru.kirillgolovko.otus.javapro.atm.hardware;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.kirillgolovko.otus.javapro.atm.model.GetResult;
import ru.kirillgolovko.otus.javapro.atm.model.Nominal;
import ru.kirillgolovko.otus.javapro.atm.model.PutResult;

/**
 * @author kirillgolovko
 */
public class MoneyStorage {
    private static final int DEFAULT_AMOUNT = 100;

    private final Map<Nominal, MoneyBox> moneyBoxes;
    private final List<Nominal> nominals;

    private MoneyStorage(List<MoneyBox> moneyBoxes) {
        Map<Nominal, MoneyBox> boxes = new HashMap<>();
        moneyBoxes.forEach(box -> boxes.put(box.getNominal(), box));
        this.nominals = boxes.keySet().stream().sorted(Comparator.comparing(Nominal::getNominal).reversed()).toList();
        this.moneyBoxes = boxes;
    }

    public static MoneyStorage getStorageWithDefaultAmount() {
        return new MoneyStorage(MoneyBox.getSupportedBoxes(DEFAULT_AMOUNT, Nominal.getSupportedNominal()));
    }

    public GetResult getMoney(BigDecimal amount) {
        Map<Nominal, Integer> toTake = new HashMap<>();
        for (var nominal : nominals) {
            MoneyBox moneyBox = moneyBoxes.get(nominal);
            int toTakeFromCurrent = 0;
            while (amount.compareTo(nominal.getNominal()) >= 0 && moneyBox.getCount() >= toTakeFromCurrent + 1) {
                toTakeFromCurrent++;
                amount = amount.add(nominal.getNominal().negate());
            }
            if(toTakeFromCurrent > 0) {
                toTake.put(nominal, toTakeFromCurrent);
            }
        }

        if (amount.equals(BigDecimal.ZERO)) {
            toTake.forEach((nominal, count) -> moneyBoxes.get(nominal).getBanknotes(count));
            return GetResult.ok(toTake);
        }
        return GetResult.failed();
    }

    public PutResult putMoney(Map<Nominal, Integer> banknotes) {
        Map<Nominal, Integer> badBanknotes = new HashMap<>();
        BigDecimal amount = BigDecimal.ZERO;
        for (Map.Entry<Nominal, Integer> entry : banknotes.entrySet()) {
            Nominal nominal = entry.getKey();
            Integer count = entry.getValue();
            if (moneyBoxes.containsKey(nominal)) {
                moneyBoxes.get(nominal).putBanknotes(count);
                amount = amount.add(nominal.getNominal().multiply(BigDecimal.valueOf(count)));
            } else {
                badBanknotes.put(nominal, count);
            }
        }
        return new PutResult(amount, badBanknotes);
    }

}
