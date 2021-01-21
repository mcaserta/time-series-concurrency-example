package com.mirkocaserta.example;

import java.time.Instant;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

final class TimeValue {

    private final Instant timestamp;

    private final double value;

    public TimeValue(Instant timestamp, double value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public static TimeValue of(String timestamp, double value) {
        return new TimeValue(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(timestamp)), value);
    }

    public static TimeValue of(Instant timestamp, double value) {
        return new TimeValue(timestamp, value);
    }

    public Instant timestamp() {
        return timestamp;
    }

    public double value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TimeValue timeValue = (TimeValue) o;
        return Double.compare(timeValue.value, value) == 0 && Objects.equals(timestamp, timeValue.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, value);
    }

    @Override
    public String toString() {
        return String.format("TimeValue{timestamp=%s, value=%.3f}", timestamp, value);
    }

    public String ts() {
        return DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }

}
