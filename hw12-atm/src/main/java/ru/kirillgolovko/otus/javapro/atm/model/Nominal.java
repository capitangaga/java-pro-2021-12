package ru.kirillgolovko.otus.javapro.atm.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author kirillgolovko
 */
public enum Nominal {
    R50(BigDecimal.valueOf(50)),
    R100(BigDecimal.valueOf(100)),
    R200(BigDecimal.valueOf(200)),
    R500(BigDecimal.valueOf(500)),
    R1000(BigDecimal.valueOf(1000)),
    R2000(BigDecimal.valueOf(2000)),
    R5000(BigDecimal.valueOf(5000));

    private final BigDecimal nominal;

    Nominal(BigDecimal nominal) {
        this.nominal = nominal;
    }

    public static List<Nominal> getSupportedNominal() {
        // without 50r
        return List.of(values()).subList(1, values().length);
    };

    public BigDecimal getNominal() {
        return nominal;
    }
}
