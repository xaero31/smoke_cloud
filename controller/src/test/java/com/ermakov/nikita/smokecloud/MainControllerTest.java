package com.ermakov.nikita.smokecloud;

import com.ermakov.nikita.smokecloud.controller.MainController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * created by Nikita_Ermakov at 2/14/2020
 */
public class MainControllerTest {

    private MainController controller;

    @BeforeEach
    void before() {
        controller = new MainController();
    }

    @Test
    void mainControllerShouldReturnIndexPage() {
        final String welcomePage = controller.getWelcomePage();
        assertEquals("index", welcomePage, "getWelcomePage should return index page");
    }
}
