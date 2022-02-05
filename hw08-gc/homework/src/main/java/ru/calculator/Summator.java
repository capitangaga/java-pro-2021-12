package ru.calculator;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class Summator {
    private int sum = 0;
    private int prevValue = 0;
    private int prevPrevValue = 0;
    private int sumLastThreeValues = 0;
    private int someValue = 0;
    private final IntList listValues = new IntArrayList();

    //!!! сигнатуру метода менять нельзя
    public void calc(Data data) {
        final int value = data.getValue();
        listValues.add(value);
        if (listValues.size() % 6_600_000 == 0) {
            listValues.clear();
        }
        sum += value;

        sumLastThreeValues = value + prevValue + prevPrevValue;

        prevPrevValue = prevValue;
        prevValue = value;

        for (var idx = 0; idx < 3; idx++) {
            someValue += (sumLastThreeValues * sumLastThreeValues / (value + 1) - sum);
            someValue = Math.abs(someValue) + listValues.size();
        }
    }

    public Integer getSum() {
        return sum;
    }

    public Integer getPrevValue() {
        return prevValue;
    }

    public Integer getPrevPrevValue() {
        return prevPrevValue;
    }

    public Integer getSumLastThreeValues() {
        return sumLastThreeValues;
    }

    public Integer getSomeValue() {
        return someValue;
    }
}
