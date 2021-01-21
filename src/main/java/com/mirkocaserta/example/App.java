package com.mirkocaserta.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class App {

    public static void main(String[] args) {
        log("Hello concurrent world!");

        TimeValueProvider provider1 = new RandomTimeValueProvider(0, 50);
        TimeValueProvider provider2 = new RandomTimeValueProvider(0, 100);

        CompletableFuture<List<TimeValue>> timedValuesFuture1 = CompletableFuture.supplyAsync(() -> {
            log("Calling provider1...");
            List<TimeValue> timeValues = provider1.get();
            log(String.format("provider 1 returned: %s\n", timeValues));
            return timeValues;
        });

        CompletableFuture<List<TimeValue>> timedValuesFuture2 = CompletableFuture.supplyAsync(() -> {
            log("Calling provider2...");
            List<TimeValue> timeValues = provider2.get();
            log(String.format("provider 2 returned: %s\n", timeValues));
            return timeValues;
        });

        log("Calling allOf(...).join()");
        CompletableFuture.allOf(timedValuesFuture1, timedValuesFuture2).join();
        log("After allOf(...).join()");

        List<TimeValue> timeValues1 = timedValuesFuture1.join();
        List<TimeValue> timeValues2 = timedValuesFuture2.join();


        List<TimeValue> timeValues = new FunctionalAirQualityIndexCalculator(40).calculate(timeValues1, timeValues2);

        log("timeValues = " + timeValues);
        System.exit(0);
    }

    private static void log(String message) {
        System.out.printf("%s --- [%s] %s\n", LocalDateTime.now(), Thread.currentThread().getName(), message);
    }

}
