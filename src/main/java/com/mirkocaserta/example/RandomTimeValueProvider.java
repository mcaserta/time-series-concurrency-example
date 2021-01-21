package com.mirkocaserta.example;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toUnmodifiableList;

public class RandomTimeValueProvider implements TimeValueProvider {

    private static final Random rng = new SecureRandom();

    private final double randomNumberOrigin;

    private final double randomNumberBound;

    public RandomTimeValueProvider(double randomNumberOrigin, double randomNumberBound) {
        this.randomNumberOrigin = randomNumberOrigin;
        this.randomNumberBound = randomNumberBound;
    }

    private TimeValue random() {
        return TimeValue.of(
                String.format("2021-01-18T08:00:%02dZ", rng.nextInt(60)),
                rng.doubles(randomNumberOrigin, randomNumberBound)
                        .findFirst()
                        .orElse(0d)
        );
    }

    @Override
    public List<TimeValue> get() {
        try {
            Thread.sleep(1000); // simulate network latency
        } catch (InterruptedException e) {
            System.err.println("Something horrible happened: " + e.getMessage());
            System.exit(1);
        }

        return Stream.generate(this::random)
                .limit(rng.ints(1, 10).findFirst().orElse(1))
                .sorted(comparing(TimeValue::timestamp))
                .collect(toUnmodifiableList());
    }

}
