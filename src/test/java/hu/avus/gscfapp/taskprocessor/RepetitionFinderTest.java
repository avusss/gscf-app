package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.provider.Arguments;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class RepetitionFinderTest {

    @Mock
    RoomEquivalencyStrategy equivalencyStrategy;

    RepetitionFinder processor;

    static Stream<Arguments> testDataStream() {
        int lineNumber = 0;
        return Stream.of(
                Arguments.of(
                        RoomRecord.builder()
                                .length(30)
                                .width(20)
                                .height(10)
                                .lineNumber(++lineNumber)
                                .build(),
                        RoomRecord.builder()
                                .length(30)
                                .width(20)
                                .height(10)
                                .lineNumber(++lineNumber)
                                .build()
                ),
                Arguments.of(
                        RoomRecord.builder()
                                .length(20)
                                .width(10)
                                .height(30)
                                .lineNumber(++lineNumber)
                                .build(),
                        RoomRecord.builder()
                                .length(20)
                                .width(10)
                                .height(30)
                                .lineNumber(++lineNumber)
                                .build()
                ),
                Arguments.of(
                        RoomRecord.builder()
                                .length(30)
                                .width(20)
                                .height(10)
                                .lineNumber(++lineNumber)
                                .build(),
                        RoomRecord.builder()
                                .length(30)
                                .width(20)
                                .height(10)
                                .lineNumber(++lineNumber)
                                .build()
                )
        );
    }

    @BeforeEach
    void setUp() {
        processor = new RepetitionFinder(equivalencyStrategy);
    }

    @Test
    void processLine() {
        testDataStream().forEach(arguments -> {
            Object[] args = arguments.get();
            RoomRecord input = (RoomRecord) args[0];
            RoomRecord output = (RoomRecord) args[1];

            Mockito.when(equivalencyStrategy.getKeyForThisRecord(input))
                    .thenReturn(output);
            processor.processLine(input);
        });

        assertEquals(2, processor.getOriginalResultMap().size());

        Integer[] resultListsizes = processor.getOriginalResultMap().values()
                .stream()
                .map(List::size)
                .toArray(Integer[]::new);
        assertArrayEquals(new Integer[]{1, 2}, resultListsizes);
    }
}