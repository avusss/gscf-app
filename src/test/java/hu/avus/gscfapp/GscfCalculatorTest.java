package hu.avus.gscfapp;

import hu.avus.gscfapp.inputdatasource.InputDataSource;
import hu.avus.gscfapp.linevalidator.LineValidator;
import hu.avus.gscfapp.linevalidator.LittlePermissiveCaseInsensitiveLineValidator;
import hu.avus.gscfapp.linevalidator.SmartLineValidator;
import hu.avus.gscfapp.linevalidator.StrictCaseSensitiveLineValidator;
import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;
import hu.avus.gscfapp.taskprocessor.CubicRoomFinder;
import hu.avus.gscfapp.taskprocessor.RoomAreaCalculatorHelper;
import hu.avus.gscfapp.taskprocessor.TaskProcessor;
import hu.avus.gscfapp.taskprocessor.TotalAreaCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GscfCalculatorIT {

    InputDataSource dataSource;
    RoomAreaCalculatorHelper roomAreaCalculator;
    RoomEquivalencyStrategy roomEquivalencyStrategy;
    GscfCalculator gscfCalculator;
    TaskProcessor<?> processor;

    @BeforeEach
    void init() {
        dataSource = getDataSource();
        roomAreaCalculator = new RoomAreaCalculatorHelper();
    }

    static Stream<Arguments> totalCalculationsDataStream() {
        return Stream.of(
                Arguments.of(new SmartLineValidator(), 10, 0, 1969L),
                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(), 10, 3, 1104L),
                Arguments.of(new StrictCaseSensitiveLineValidator(), 10, 5, 761L)
        );
    }

    static Stream<Arguments> cubicRoomCalculationsDataStream() {
        return Stream.of(
                Arguments.of(new SmartLineValidator(), 4, List.of(7, 6, 5, 5)),
                Arguments.of(new LittlePermissiveCaseInsensitiveLineValidator(), 2, List.of(7, 5)),
                Arguments.of(new StrictCaseSensitiveLineValidator(), 1, List.of(7))
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

    private InputDataSource getDataSource() {
        return () -> List.of(
                "1x2x3",
                "2x3x4",
                "4X5X6",
                "lorem7ipsum8dolor9amet",
                "lorem5ipsum5dolor5amet",
                "6,6,6",
                "5x4x6",
                "6x4x5",
                "7x7x7",
                "5x5X5"
        ).iterator();
    }
}