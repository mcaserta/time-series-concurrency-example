package com.mirkocaserta.example;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;

public class FunctionalAirQualityIndexCalculator implements AirQualityIndexCalculator {

    private final double maxTemperature;

    public FunctionalAirQualityIndexCalculator(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Override
    public List<TimeValue> calculate(List<TimeValue> temperatures, List<TimeValue> carbonMonoxidePercentages) {
        Stream<TypedTimeValue> timeSeries = Stream.concat(
                temperatures.stream().map(e -> new TypedTimeValue(TypedTimeValue.Type.T, e)),
                carbonMonoxidePercentages.stream().map(e -> new TypedTimeValue(TypedTimeValue.Type.C, e))
        );

        return timeSeries.sorted(comparing(TypedTimeValue::timestamp))
                .collect(AirQualityIndexCollector.toUnmodifiableList(maxTemperature));
    }

}
