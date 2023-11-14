package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StrictRoomEquivalencyStrategyTest {

    static Stream<Arguments> testDataStream() {
        return Stream.of(
                Arguments.of(
                        RoomRecord.builder()
                                .length(10)
                                .width(20)
                                .height(30)
                                .build(),
                        RoomRecord.builder()
                                .length(10)
                                .width(20)
                                .height(30)
                                .build()
                ),
                Arguments.of(
                        RoomRecord.builder()
                                .length(20)
                                .width(10)
                                .height(30)
                                .build(),
                        RoomRecord.builder()
                                .length(20)
                                .width(10)
                                .height(30)
                                .build()
                ),
                Arguments.of(
                        RoomRecord.builder()
                                .length(30)
                                .width(20)
                                .height(10)
                                .build(),
                        RoomRecord.builder()
                                .length(30)
                                .width(20)
                                .height(10)
                                .build()
                )
        );
    }

    RoomEquivalencyStrategy strategy;

    @BeforeEach
    void init() {
        strategy = new StrictRoomEquivalencyStrategy();
    }

    @ParameterizedTest(name = "{index} => input={0}, expected={1}")
    @MethodSource("testDataStream")
    void addRoomToMap(RoomRecord input, RoomRecord expected) {
        RoomRecord result = strategy.getKeyForThisRecord(input);
        assertEquals(expected, result);
    }
}