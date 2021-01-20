package com.mirkocaserta.example;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toUnmodifiableList;

public class RandomTimedValueProvider implements TimedValueProvider {

    private static final Random rng = new SecureRandom();

    private final double randomNumberOrigin;

    private final double randomNumberBound;

    public RandomTimedValueProvider(double randomNumberOrigin, double randomNumberBound) {
        this.randomNumberOrigin = randomNumberOrigin;
        this.randomNumberBound = randomNumberBound;
    }

    private TimedValue random() {
        return TimedValue.of(
                String.format("2021-01-18T08:00:%02dZ", rng.nextInt(60)),
                rng.doubles(randomNumberOrigin, randomNumberBound)
                        .findFirst()
                        .orElse(0d)
        );
    }

    @Override
    public List<TimedValue> get() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            System.err.println("Something horrible happened: " + e.getMessage());
            System.exit(1);
        }

        return Stream.generate(this::random)
                .limit(rng.ints(1, 10).findFirst().orElse(1))
                .sorted(comparing(TimedValue::timestamp))
                .collect(toUnmodifiableList());
    }

}
