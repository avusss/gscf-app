package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TotalAreaCalculatorTest {

    TaskProcessor<Long> processor;

    static Stream<RoomRecord> testDataStream() {
        return Stream.of(
                RoomRecord.builder()
                        .customArea(500)
                        .build(),
                RoomRecord.builder()
                        .customArea(700)
                        .build(),
                RoomRecord.builder()
                        .customArea(900)
                        .build(),
                RoomRecord.builder()
                        .build()
        );
    }


    @BeforeEach
    void init() {
        processor = new TotalAreaCalculator();
    }

    @Test
    void processLine() {
        testDataStream()
                .forEach(roomRecord -> processor.processLine(roomRecord));
        assertEquals(2100L, processor.getResults());
    }
}