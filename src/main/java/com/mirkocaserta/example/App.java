package com.mirkocaserta.example;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static java.util.Comparator.comparing;

public class App {

    public static void main(String[] args) {
        p("Hello concurrent world!");

        TimedValueProvider provider1 = new RandomTimedValueProvider();
        TimedValueProvider provider2 = new RandomTimedValueProvider();

        CompletableFuture<List<TimedValue>> timedValuesFuture1 = CompletableFuture.supplyAsync(() -> {
            p("Calling provider1...");
            List<TimedValue> timedValues = provider1.get();
            p(String.format("provider 1 returned: %s\n", timedValues));
            return timedValues;
        });

        CompletableFuture<List<TimedValue>> timedValuesFuture2 = CompletableFuture.supplyAsync(() -> {
            p("Calling provider2...");
            List<TimedValue> timedValues = provider2.get();
            p(String.format("provider 2 returned: %s\n", timedValues));
            return timedValues;
        });

        p("Calling allOf(...).join()");
        CompletableFuture.allOf(timedValuesFuture1, timedValuesFuture2).join();
        p("After allOf(...).join()");

        List<TimedValue> timedValues1 = timedValuesFuture1.join();
        List<TimedValue> timedValues2 = timedValuesFuture2.join();

        List<TimedValue> timedValues = timedValues1.stream()
                .map(tv1 -> timedValues2.stream()
                        .filter(tv2 -> tv2.timestamp().isBefore(tv1.timestamp()))
                        .max(comparing(TimedValue::timestamp))
                        .map(tv2 -> new TimedValue(tv1.timestamp(), tv1.value() * tv2.value())))
                .flatMap(Optional::stream)
                .sorted(comparing(TimedValue::timestamp))
                .collect(Collectors.toUnmodifiableList());

        p("timedValues = " + timedValues);
    }

    private static void p(String message) {
        System.out.printf("%s --- [%s] %s\n", LocalDateTime.now(), Thread.currentThread().getName(), message);
    }

}
