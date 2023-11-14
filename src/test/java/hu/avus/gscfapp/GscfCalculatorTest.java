package hu.avus.gscfapp;

import hu.avus.gscfapp.inputdatasource.InputDataSource;
import hu.avus.gscfapp.linevalidator.LineValidator;
import hu.avus.gscfapp.linevalidator.LittlePermissiveCaseInsensitiveLineValidator;
import hu.avus.gscfapp.linevalidator.SmartLineValidator;
import hu.avus.gscfapp.linevalidator.StrictCaseSensitiveLineValidator;
import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.FlexibleRoomEquivalencyStrategy;
import hu.avus.gscfapp.roomequivalency.RoomBaseEquivalencyStrategy;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;
import hu.avus.gscfapp.roomequivalency.StrictRoomEquivalencyStrategy;
import hu.avus.gscfapp.taskprocessor.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GscfCalculatorTest {

    InputDataSource dataSource;
    RoomAreaCalculatorHelper roomAreaCalculator;
    GscfCalculator gscfCalculator;
    TaskProcessor<?> processor;

    @BeforeEach
    void init() {
        dataSource = getDataSource();
        roomAreaCalculator = new RoomAreaCalculatorHelper();
    }

    static Stream<Arguments> totalCalculationsDataStream() {
        return Stream.of(
                Arguments.of(new SmartLineValidator(), 22, 1, 2868L),
                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(), 22, 6, 1829L),
                Arguments.of(new StrictCaseSensitiveLineValidator(), 22, 11, 1254L)
        );
    }

    static Stream<Arguments> cubicRoomCalculationsDataStream() {
        return Stream.of(
                Arguments.of(new SmartLineValidator(), 4, List.of(7, 6, 5, 5)),
                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(), 2, List.of(7, 5)),
                Arguments.of(new StrictCaseSensitiveLineValidator(), 1, List.of(7))
        );
    }

    static Stream<Arguments> repetitionCalculationsDataStream() {
        return Stream.of(
                Arguments.of(new SmartLineValidator(), new FlexibleRoomEquivalencyStrategy(), 4),
                Arguments.of(new SmartLineValidator(), new RoomBaseEquivalencyStrategy(), 5),
                Arguments.of(new SmartLineValidator(), new StrictRoomEquivalencyStrategy(), 4),

                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(),
                        new FlexibleRoomEquivalencyStrategy(), 3),
                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(),
                        new RoomBaseEquivalencyStrategy(), 3),
                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(),
                        new StrictRoomEquivalencyStrategy(), 2),

                Arguments.of(new StrictCaseSensitiveLineValidator(),
                        new FlexibleRoomEquivalencyStrategy(), 3),
                Arguments.of(new StrictCaseSensitiveLineValidator(),
                        new RoomBaseEquivalencyStrategy(), 1),
                Arguments.of(new StrictCaseSensitiveLineValidator(),
                        new StrictRoomEquivalencyStrategy(), 1)
        );
    }

    @ParameterizedTest(name = "{index} => validator={0}, allLines={1}, invalidLines={2}, total={3}")
    @MethodSource("totalCalculationsDataStream")
    void testTotalCalculations(LineValidator validator, int allLines, int invalidLines, long total) {
        // given
        processor = new TotalAreaCalculator();
        gscfCalculator = new GscfCalculator(dataSource, validator, roomAreaCalculator, List.of(processor));

        // when
        gscfCalculator.performCalculations();

        // then
        assertEquals(allLines, gscfCalculator.getAllLines());
        assertEquals(invalidLines, gscfCalculator.getInvalidLines());
        assertEquals(total, processor.getResults());
    }

    @ParameterizedTest(name = "{index} => validator={0}, count={1}, lengths={2}")
    @MethodSource("cubicRoomCalculationsDataStream")
    void testCubicRoomCalculations(LineValidator validator, int count, List<Integer> lengths) {
        // given
        processor = new CubicRoomFinder();
        gscfCalculator = new GscfCalculator(dataSource, validator, roomAreaCalculator, List.of(processor));

        // when
        gscfCalculator.performCalculations();

        // then
        Set<RoomRecord> results = ((CubicRoomFinder)processor).getResults();
        List<Integer> resultsList = results
                .stream()
                .map(RoomRecord::length)
                .toList();
        assertEquals(count, results.size());
        assertEquals(lengths, resultsList);
    }

    @ParameterizedTest(name = "{index} => validator={0}, strategy={1}, mapCount={2}")
    @MethodSource("repetitionCalculationsDataStream")
    void testRepetitionCalculations(LineValidator validator, RoomEquivalencyStrategy strategy, int mapCount) {
        // given
        processor = new RepetitionFinder(strategy);
        gscfCalculator = new GscfCalculator(dataSource, validator, roomAreaCalculator, List.of(processor));

        // when
        gscfCalculator.performCalculations();

        // then
        Map<RoomRecord, List<RoomRecord>> results = ((RepetitionFinder)processor).getResults();
        assertEquals(mapCount, results.size());
    }

    private InputDataSource getDataSource() {
        return () -> List.of(
                "1x2x3",
                "2x3x4",
                "4X5X6",
                "lorem7ipsum8dolor9amet",
                "lorem5ipsum5dolor5amet",
                "6,6,6",
                "5x1x6",
                "5x4x6",
                "6x4x5",
                "7x7x7",
                "5x5X5",
                "1x5x6",
                "1X5X6",
                "1,5,6",
                "5x1x6",
                "5X1X6",
                "6x1x5",
                "6,1,5",
                "full_invalid",
                "3X2x4",
                "4x3x2",
                "5x1x6"
        ).iterator();
    }
}