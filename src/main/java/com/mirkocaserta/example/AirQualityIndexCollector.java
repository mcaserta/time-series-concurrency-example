package com.mirkocaserta.example;

import java.time.Instant;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.mirkocaserta.example.AirQualityIndexCalculator.airQualityIndex;
import static com.mirkocaserta.example.AirQualityIndexCalculator.mostRecent;
import static java.util.Collections.emptySet;

public class AirQualityIndexCollector
        implements Collector<TypedTimeValue, List<TypedTimeValue>, List<TimeValue>> {

    private final Map<Instant, TimeValue> airQualityIndices = new HashMap<>();

    private final double maxTemperature;

    private TimeValue lastTemperature;

    private TimeValue lastCarbonMonoxidePercentage;

    private AirQualityIndexCollector(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public static AirQualityIndexCollector toUnmodifiableList(double maxTemperature) {
        return new AirQualityIndexCollector(maxTemperature);
    }

    @Override
    public Supplier<List<TypedTimeValue>> supplier() {
        return ArrayList::new;
    }

    @Override
    public BiConsumer<List<TypedTimeValue>, TypedTimeValue> accumulator() {
        return (typedTimeValues, e) -> {
            switch (e.type()) {
                case T:
                    lastTemperature = e.timeValue();
                    break;
                case C:
                    lastCarbonMonoxidePercentage = e.timeValue();
                    break;
            }

            if (lastTemperature != null && lastCarbonMonoxidePercentage != null) {
                Instant timestamp = mostRecent(lastTemperature.timestamp(), lastCarbonMonoxidePercentage.timestamp());
                airQualityIndices.put(
                        timestamp,
                        TimeValue.of(
                                timestamp,
                                airQualityIndex(lastTemperature.value(), lastCarbonMonoxidePercentage.value(), maxTemperature)
                        ));
                typedTimeValues.add(e);
            }
        };
    }

    @Override
    public BinaryOperator<List<TypedTimeValue>> combiner() {
        return (typedTimeValues, typedTimeValues2) -> {
            typedTimeValues.addAll(typedTimeValues2);
            return typedTimeValues;
        };
    }

    @Override
    public Function<List<TypedTimeValue>, List<TimeValue>> finisher() {
        return typedTimeValues -> airQualityIndices.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .map(e -> TimeValue.of(e.getKey(), e.getValue().value()))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Set<Characteristics> characteristics() {
        return emptySet(); // Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED);
    }

}
