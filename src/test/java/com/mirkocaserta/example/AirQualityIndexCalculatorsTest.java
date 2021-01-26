package com.mirkocaserta.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

import static com.mirkocaserta.example.TestDataProvider.*;
import static java.util.concurrent.CompletableFuture.supplyAsync;
import static org.junit.jupiter.api.Assertions.*;

class AirQualityIndexCalculatorsTest {

    @Test
    void oldSchoolCalculator() {
        AirQualityIndexCalculator calculator = new OldSchoolAirQualityIndexCalculator(MAX_TEMPERATURE);
        List<TimeValue> airQualityIndices = calculator.calculate(temperatures(), carbonMonoxidePercentages());
        assertAll(airQualityIndices);
    }

    @Test
    void functionalCalculator() {
        AirQualityIndexCalculator calculator = new FunctionalAirQualityIndexCalculator(MAX_TEMPERATURE);
        List<TimeValue> airQualityIndices = calculator.calculate(temperatures(), carbonMonoxidePercentages());
        assertAll(airQualityIndices);
    }

    @ParameterizedTest(name = "Executing {0} concurrent runs")
    @ValueSource(ints = 1000)
    void massiveConcurrency(int runs) {
        int actualRuns = IntStream.range(0, runs)
                .mapToObj((a) -> {
                    final CompletableFuture<Integer> f1 = supplyAsync(() -> {
                        oldSchoolCalculator();
                        return 1;
                    });
                    final CompletableFuture<Integer> f2 = supplyAsync(() -> {
                        functionalCalculator();
                        return 1;
                    });
                    return f1.thenCombine(f2, Integer::sum);
                })
                .mapToInt(CompletableFuture::join)
                .sum();

        assertEquals(runs * 2, actualRuns);
    }

    private void assertAll(List<TimeValue> airQualityIndices) {
        assertNotNull(airQualityIndices);
        assertEquals(9, airQualityIndices.size(), "size");
        // Assert that expected and actual iterables are deeply equal.
        // Deeply equal means that number and order of elements in collection
        // must be the same; iterated elements must be equal as well.
        assertIterableEquals(airQualityIndices(), airQualityIndices, "airQualityIndices");
    }

}