package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcessorAggregator implements Processor {

    @Override
    public Map<String, Double> process(List<Measurement> data) {
        Map<String, Double> result = new HashMap<>();
        data.forEach(measurement -> result.merge(measurement.getName(), measurement.getValue(), Double::sum));
        return Collections.unmodifiableMap(result);
    }
}
