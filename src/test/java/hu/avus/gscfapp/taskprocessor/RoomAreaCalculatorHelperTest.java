package hu.avus.gscfapp.taskprocessor;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class RoomAreaCalculatorHelperTest {

    static Stream<Arguments> testDataStream() {
        return Stream.of(
                Arguments.of(1, 2, 3, 24),
                Arguments.of(1, 1, 5, 23),
                Arguments.of(8, 9, 2, 228),
                Arguments.of(9, 5, 4, 222)
        );
    }

    RoomAreaCalculatorHelper helper;

    @BeforeEach
    void init() {
        helper = new RoomAreaCalculatorHelper();
    }

    @ParameterizedTest(name = "{index} => l={0}, w={1}, h={2}, area={4}")
    @MethodSource("testDataStream")
    void calculateArea(int l, int w, int h, int area) {
        int result = helper.calculateArea(l, w, h);
        Assertions.assertEquals(area, result);
    }
}