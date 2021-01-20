package com.mirkocaserta.example;

import java.security.SecureRandom;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RandomTimedValueProvider implements TimedValueProvider {

    private static final Random rng = new SecureRandom();

    private static TimedValue random() {
        return TimedValue.of(
                String.format("2021-01-18T08:00:%02dZ", rng.nextInt(60)),
                rng.doubles(-10.0, 10.0)
                        .findFirst()
                        .orElse(0d)
        );
    }

    @Override
    public List<TimedValue> get() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // not gonna happen
        }
        return Stream.generate(RandomTimedValueProvider::random)
                .limit(rng.ints(1, 10).findFirst().orElse(1))
                .collect(Collectors.toUnmodifiableList());
    }

}
