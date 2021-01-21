package com.mirkocaserta.example;

import java.util.List;

public abstract class TestDataProvider {

    public static List<TimeValue> temperatures() {
        return List.of(
                TimeValue.of("2021-01-20T08:00:00Z", 10.1),
                TimeValue.of("2021-01-20T08:02:00Z", 10.3),
                TimeValue.of("2021-01-20T08:05:00Z", 10.7),
                TimeValue.of("2021-01-20T08:06:00Z", 10.9),
                TimeValue.of("2021-01-20T08:06:19Z", 11.0),
                TimeValue.of("2021-01-20T08:06:42Z", 11.1),
                TimeValue.of("2021-01-20T08:09:00Z", 11.3)
        );
    }

    public static List<TimeValue> carbonMonoxidePercentages() {
        return List.of(
                TimeValue.of("2021-01-20T08:01:00Z", 2.0),
                TimeValue.of("2021-01-20T08:02:00Z", 2.3),
                TimeValue.of("2021-01-20T08:06:00Z", 2.8),
                TimeValue.of("2021-01-20T08:07:00Z", 2.9),
                TimeValue.of("2021-01-20T08:08:00Z", 3.3)
        );
    }

    public static List<TimeValue> airQualityIndices() {
        return List.of(
                TimeValue.of("2021-01-20T08:01:00Z", 13.625),
                TimeValue.of("2021-01-20T08:02:00Z", 14.025),
                TimeValue.of("2021-01-20T08:05:00Z", 14.525),
                TimeValue.of("2021-01-20T08:06:00Z", 15.025),
                TimeValue.of("2021-01-20T08:06:19Z", 15.150),
                TimeValue.of("2021-01-20T08:06:42Z", 15.275),
                TimeValue.of("2021-01-20T08:07:00Z", 15.325),
                TimeValue.of("2021-01-20T08:08:00Z", 15.525),
                TimeValue.of("2021-01-20T08:09:00Z", 15.775)
        );
    }

}
