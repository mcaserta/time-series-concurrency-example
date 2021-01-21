package com.mirkocaserta.example;

import org.junit.jupiter.api.Test;

import java.util.List;

import static com.mirkocaserta.example.TestDataProvider.*;
import static org.junit.jupiter.api.Assertions.*;

class AirQualityIndexCalculatorsTest {

    @Test
    void testOldSchoolCalculator() {
        AirQualityIndexCalculator calculator = new OldSchoolAirQualityIndexCalculator(40);
        List<TimeValue> airQualityIndices = calculator.calculate(temperatures(), carbonMonoxidePercentages());
        assertAll(airQualityIndices);
    }

    @Test
    void testFunctionalCalculator() {
        AirQualityIndexCalculator calculator = new FunctionalAirQualityIndexCalculator(40);
        List<TimeValue> airQualityIndices = calculator.calculate(temperatures(), carbonMonoxidePercentages());
        assertAll(airQualityIndices);
    }

    private void assertAll(List<TimeValue> airQualityIndices) {
        System.out.println("airQualityIndices = " + airQualityIndices);
        assertNotNull(airQualityIndices);
        assertEquals(9, airQualityIndices.size(), "size");
        // It asserts that expected and actual iterables are deeply equal.
        // Deeply equal means that number and order of elements in collection
        // must be same; as well as iterated elements must be equal.
        assertIterableEquals(airQualityIndices(), airQualityIndices, "airQualityIndices");
    }

}