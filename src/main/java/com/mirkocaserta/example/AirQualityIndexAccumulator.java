package com.mirkocaserta.example;

import java.time.Instant;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mirkocaserta.example.AirQualityIndexCalculator.airQualityIndex;
import static com.mirkocaserta.example.AirQualityIndexCalculator.mostRecent;
import static java.util.stream.Collectors.toUnmodifiableList;

public class AirQualityIndexAccumulator {

    private final Map<Instant, TimeValue> airQualityIndices = new HashMap<>();

    private final double maxTemperature;

    private TimeValue lastTemperature;

    private TimeValue lastCarbonMonoxidePercentage;

    public AirQualityIndexAccumulator(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @SuppressWarnings("unused")
    public static void combine(AirQualityIndexAccumulator a1, AirQualityIndexAccumulator a2) {
        // this is required by the collect method but is not really used
    }

    public void accept(TypedTimeValue e) {
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
                    )
            );
        }
    }

    public List<TimeValue> getAirQualityIndices() {
        return airQualityIndices.values().stream()
                .sorted(Comparator.comparing(TimeValue::timestamp))
                .collect(toUnmodifiableList());
    }

}
