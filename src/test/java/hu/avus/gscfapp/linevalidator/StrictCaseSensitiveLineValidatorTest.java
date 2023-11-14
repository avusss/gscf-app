package hu.avus.gscfapp.linevalidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrictCaseSensitiveLineValidatorTest {

    private LineValidator validator;

    @BeforeEach
    void init() {
        validator = new StrictCaseSensitiveLineValidator();
    }

    @ParameterizedTest
    @ValueSource(strings = {"3x11x24"})
    void shouldBeValid(String line) {
        assertTrue(validator.isValid(line));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "26", "12x8", " 29x26x12", "29x26x12 ", "4x5x6x", "4,5,6", "26X6x11", "12x8X17", "29X26X12"})
    void shouldBeInvalid(String line) {
        assertFalse(validator.isValid(line));
    }
}