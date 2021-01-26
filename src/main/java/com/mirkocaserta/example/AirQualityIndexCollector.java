package com.mirkocaserta.example;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static com.mirkocaserta.example.AirQualityIndexCalculator.airQualityIndex;
import static com.mirkocaserta.example.AirQualityIndexCalculator.mostRecent;
import static java.util.Comparator.comparing;

public class AirQualityIndexCollector
        implements Collector<TypedTimeValue, Queue<TypedTimeValue>, List<TimeValue>> {

    private final double maxTemperature;

    private AirQualityIndexCollector(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    public static AirQualityIndexCollector toUnmodifiableList(double maxTemperature) {
        return new AirQualityIndexCollector(maxTemperature);
    }

    @Override
    public Supplier<Queue<TypedTimeValue>> supplier() {
        return ConcurrentLinkedQueue::new;
    }

    @Override
    public BiConsumer<Queue<TypedTimeValue>, TypedTimeValue> accumulator() {
        return Queue::add;
    }

    @Override
    public BinaryOperator<Queue<TypedTimeValue>> combiner() {
        return (typedTimeValues, typedTimeValues2) -> {
            typedTimeValues.addAll(typedTimeValues2);
            return typedTimeValues;
        };
    }

    @Override
    public Function<Queue<TypedTimeValue>, List<TimeValue>> finisher() {
        final Map<Instant, TimeValue> aqiAccumulator = new HashMap<>();

        return accumulator -> {
            accumulator.stream()
                    .map(TypedTimeValue::timestamp)
                    .sorted()
                    .forEach(entryTS -> {
                        final TimeValue lastTemperature = getClosest(accumulator, TypedTimeValue.Type.T, entryTS);
                        final TimeValue lastCarbonMonoxidePercentage = getClosest(accumulator, TypedTimeValue.Type.C, entryTS);

                        if (lastTemperature != null && lastCarbonMonoxidePercentage != null) {
                            Instant timestamp = mostRecent(lastTemperature.timestamp(), lastCarbonMonoxidePercentage.timestamp());
                            aqiAccumulator.put(timestamp, TimeValue.of(timestamp, airQualityIndex(lastTemperature.value(), lastCarbonMonoxidePercentage.value(), maxTemperature)));
                        }
                    });

            return aqiAccumulator.values().stream()
                    .sorted()
                    .collect(Collectors.toUnmodifiableList());
        };
    }

    @Override
    public Set<Characteristics> characteristics() {
        return Set.of(Characteristics.CONCURRENT, Characteristics.UNORDERED);
    }

    private static TimeValue getClosest(Queue<TypedTimeValue> accumulator, TypedTimeValue.Type type, Instant timestamp) {
        return accumulator.stream()
                .filter(e -> e.type().equals(type))
                .filter(e -> e.timestamp().equals(timestamp) || e.timestamp().isBefore(timestamp))
                .sorted()
                .max(comparing(TypedTimeValue::timestamp))
                .map(TypedTimeValue::timeValue)
                .orElse(null);
    }

}
