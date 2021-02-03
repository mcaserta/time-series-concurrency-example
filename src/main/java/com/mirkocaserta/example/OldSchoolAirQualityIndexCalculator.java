package com.mirkocaserta.example;

import java.time.Instant;
import java.util.*;

import static com.mirkocaserta.example.AirQualityIndexCalculator.airQualityIndex;
import static com.mirkocaserta.example.AirQualityIndexCalculator.mostRecent;
import static java.util.Comparator.comparing;

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

        Map<String, TimeValue> timeValuesByTypeSortedByTimestamp = new LinkedHashMap<>();
        List<String> keysSortedByTimestamp = new ArrayList<>(timeValuesByType.keySet());
        keysSortedByTimestamp.sort(comparing(s -> timeValuesByType.get(s).timestamp()));

        for (String key : keysSortedByTimestamp) {
            timeValuesByTypeSortedByTimestamp.put(key, timeValuesByType.get(key));
        }

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

        List<Instant> keys = new ArrayList<>(airQualityIndexMap.keySet());
        keys.sort(Instant::compareTo);
        List<TimeValue> results = new ArrayList<>();

        for (Instant key : keys) {
            results.add(TimeValue.of(key, airQualityIndexMap.get(key)));
        }

        return results;
    }

}
