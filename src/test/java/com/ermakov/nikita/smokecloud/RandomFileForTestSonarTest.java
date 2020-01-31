package com.ermakov.nikita.smokecloud;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * created by Nikita_Ermakov at 1/31/2020
 */
class RandomFileForTestSonarTest {

    private RandomFileForTestSonar object;

    @BeforeEach
    void before() {
        object = new RandomFileForTestSonar();
    }

    @Test
    void testOne() {
        Assertions.assertEquals(object.returnStringByEnvironment(true), "Condition is true");
    }
}
