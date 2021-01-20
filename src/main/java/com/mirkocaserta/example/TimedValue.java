package com.mirkocaserta.example;

import java.time.Instant;
import java.time.format.DateTimeFormatter;

public record TimedValue(Instant timestamp, double value) {

    public static TimedValue of(String timestamp, double value) {
        return new TimedValue(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(timestamp)), value);
    }

    public String ts() {
        return DateTimeFormatter.ISO_INSTANT.format(timestamp);
    }

}
