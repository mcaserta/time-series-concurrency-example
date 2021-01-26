package com.mirkocaserta.example;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FunctionalAirQualityIndexCalculator implements AirQualityIndexCalculator {

    private final double maxTemperature;

    public FunctionalAirQualityIndexCalculator(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Override
    public List<TimeValue> calculate(List<TimeValue> temperatures, List<TimeValue> carbonMonoxidePercentages) {
        List<TypedTimeValue> timeSeries = Stream.concat(
                temperatures.stream().map(e -> new TypedTimeValue(TypedTimeValue.Type.T, e)),
                carbonMonoxidePercentages.stream().map(e -> new TypedTimeValue(TypedTimeValue.Type.C, e))
        ).collect(Collectors.toUnmodifiableList());

        return timeSeries.stream().parallel()
                .collect(AirQualityIndexCollector.toUnmodifiableList(maxTemperature));
    }

}
