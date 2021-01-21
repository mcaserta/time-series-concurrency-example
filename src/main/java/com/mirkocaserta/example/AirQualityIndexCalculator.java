package com.mirkocaserta.example;

import java.time.Instant;
import java.util.List;

public interface AirQualityIndexCalculator {

    static Instant mostRecent(Instant i1, Instant i2) {
        if (i1.equals(i2)) {
            return i1;
        } else if (i1.isAfter(i2)) {
            return i1;
        } else {
            return i2;
        }
    }

    static double airQualityIndex(double temperature, double carbonMonoxidePercentage, double maxTemperature) {
        return (((temperature * 100) / maxTemperature) + carbonMonoxidePercentage) / 2;
    }

    List<TimeValue> calculate(List<TimeValue> temperatures, List<TimeValue> carbonMonoxidePercentages);

}
