package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CubicRoomFinderTest {

    TaskProcessor<Set<RoomRecord>> processor;

    static Stream<RoomRecord> testDataStream() {
        int lineNumber = 0;
        return Stream.of(
                RoomRecord.builder()
                        .length(10)
                        .width(10)
                        .height(10)
                        .lineNumber(++lineNumber)
                        .build(),
                RoomRecord.builder()
                        .length(40)
                        .width(40)
                        .height(40)
                        .lineNumber(++lineNumber)
                        .build(),
                RoomRecord.builder()
                        .length(5)
                        .width(5)
                        .height(5)
                        .lineNumber(++lineNumber)
                        .build(),
                RoomRecord.builder()
                        .length(5)
                        .width(6)
                        .height(7)
                        .lineNumber(++lineNumber)
                        .build()
        );
    }

    @BeforeEach
    void init() {
        processor = new CubicRoomFinder();
    }

    @Test
    void processLine() {
        testDataStream()
                .forEach(roomRecord -> processor.processLine(roomRecord));
        assertEquals(3, processor.getResults().size());
    }
}