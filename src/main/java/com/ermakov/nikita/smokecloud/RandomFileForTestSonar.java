package com.ermakov.nikita.smokecloud;

/**
 * created by Nikita_Ermakov at 1/31/2020
 */
public class RandomFileForTestSonar {

    public String returnStringByEnvironment(boolean condition) {
        if (condition) {
            return "Condition is true";
        } else {
            return "Condition is false";
        }
    }
}
