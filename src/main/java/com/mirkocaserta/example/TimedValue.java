package com.mirkocaserta.example;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class TimedValue {

    private final Instant timestamp;

    private final double value;

    public TimedValue(Instant timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public static TimedValue of(String timestamp, double value) {
        return new TimedValue(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(timestamp)), value);
    }

    public Instant timestamp() {
        return timestamp;
    }

    public double value() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("TimedValue{timestamp=%s, value=%.3f}", timestamp, value);
    }

    public String ts() {
        return DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }

}
