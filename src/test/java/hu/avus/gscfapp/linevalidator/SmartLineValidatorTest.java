package hu.avus.gscfapp.linevalidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SmartLineValidatorTest {

    private LineValidator validator;

    @BeforeEach
    void init() {
        validator = new SmartLineValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"3x11x24", "3,11 sfdfsf 24", "    lorem1ipsum2dolor3sit    "})
    void shouldBeValid(String line) {
        assertTrue(validator.isValid(line));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "26", "12x8", "1x2x3x4", "    lorem 1 ipsum 2 dolor 3 sit 4 amet  "})
    void shouldBeInvalid(String line) {
        assertFalse(validator.isValid(line));
    }
}