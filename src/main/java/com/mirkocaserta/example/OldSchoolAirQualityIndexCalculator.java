package com.mirkocaserta.example;

import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.mirkocaserta.example.AirQualityIndexCalculator.airQualityIndex;
import static com.mirkocaserta.example.AirQualityIndexCalculator.mostRecent;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toUnmodifiableList;

public class OldSchoolAirQualityIndexCalculator implements AirQualityIndexCalculator {

    private final double maxTemperature;

    public OldSchoolAirQualityIndexCalculator(double maxTemperature) {
        this.maxTemperature = maxTemperature;
    }

    @Override
    public List<TimeValue> calculate(List<TimeValue> temperatures, List<TimeValue> carbonMonoxidePercentages) {
        // key = time value type (C = carbonMonoxidePercentage, T = temperature)
        // concatenated with the timestamp as a string
        Map<String, TimeValue> timeValuesByType = new HashMap<>();

        for (TimeValue temperature : temperatures) {
            timeValuesByType.put("T".concat(temperature.ts()), temperature);
        }

        for (TimeValue carbonMonoxidePercentage : carbonMonoxidePercentages) {
            timeValuesByType.put("C".concat(carbonMonoxidePercentage.ts()), carbonMonoxidePercentage);
        }

        Map<String, TimeValue> timeValuesByTypeSortedByTimestamp = timeValuesByType.entrySet().stream()
                .sorted(comparing((tv) -> tv.getValue().timestamp()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (oldValue, newValue) -> oldValue, LinkedHashMap::new));

        Map<Instant, Double> airQualityIndexMap = new HashMap<>();
        TimeValue lastTemperature = null;
        TimeValue lastCarbonMonoxidePercentage = null;

        for (Map.Entry<String, TimeValue> entry : timeValuesByTypeSortedByTimestamp.entrySet()) {
            if (entry.getKey().startsWith("T")) {
                lastTemperature = entry.getValue();
            } else if (entry.getKey().startsWith("C")) {
                lastCarbonMonoxidePercentage = entry.getValue();
            }

            if (lastTemperature != null && lastCarbonMonoxidePercentage != null) {
                airQualityIndexMap.put(
                        mostRecent(lastTemperature.timestamp(), lastCarbonMonoxidePercentage.timestamp()),
                        airQualityIndex(lastTemperature.value(), lastCarbonMonoxidePercentage.value(), maxTemperature)
                );
            }
        }

        return airQualityIndexMap.entrySet().stream()
                .map(e -> new TimeValue(e.getKey(), e.getValue()))
                .sorted(comparing(TimeValue::timestamp))
                .collect(toUnmodifiableList());
    }

}
