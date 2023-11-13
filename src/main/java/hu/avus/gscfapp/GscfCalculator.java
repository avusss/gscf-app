package hu.avus.gscfapp;

import hu.avus.gscfapp.inputdatasource.InputDataSource;
import hu.avus.gscfapp.linevalidator.LineValidator;
import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;
import hu.avus.gscfapp.taskprocessor.*;

import java.util.*;
import java.util.stream.Collectors;

public class GscfCalculator {

    public static final String SEPARATOR =
            "--------------------------------------------------------------------------------";

    private final Iterator<String> iterator;
    private final LineValidator validator;
    private final RoomAreaCalculatorHelper areaCalculator;
    private final List<TaskProcessor<?>> taskProcessors;
    int invalidLines = 0;
    int allLines = 0;

    public GscfCalculator(
            InputDataSource dataSource,
            LineValidator validator,
            RoomAreaCalculatorHelper roomAreaCalculator,
            RoomEquivalencyStrategy roomEquivalencyStrategy) {
        this.iterator = dataSource.iterator();
        this.validator = validator;
        this.areaCalculator = roomAreaCalculator;
        this.taskProcessors = List.of(
                new TotalAreaCalculator().setResultHandler(this::printTotalResult),
                new CubicRoomFinder().setResultHandler(this::printCubicShapedRooms),
                new RepetitionFinder(roomEquivalencyStrategy).setResultHandler(this::printRepetitiveRooms)
        );
    }

    public void performCalculations() {
        while (iterator.hasNext()) {
            String line = iterator.next();
            processLine(line);
        }

        printLineReport();
        taskProcessors.forEach(TaskProcessor::handleResults);
    }

    private void processLine(String line) {
        allLines++;
        final boolean isValid = validator.isValid(line);
        if (isValid) {
            List<Integer> dims =
                    Arrays.stream(line.toLowerCase().split("\\D+"))
                            .filter(s -> !s.isEmpty())
                            .map(Integer::parseInt)
                            .toList();
            RoomRecord roomRecord = new RoomRecord(dims.get(0), dims.get(1), dims.get(2), allLines,
                    areaCalculator.calculateArea(dims.get(0), dims.get(1), dims.get(2)));
            taskProcessors.forEach(taskProcessor -> taskProcessor.processLine(roomRecord));
        } else {
            invalidLines++;
        }
    }

    private void printLineReport() {
        System.out.printf("%d valid and %d invalid lines in input%n", allLines - invalidLines, invalidLines);
    }

    private void printTotalResult(long total) {
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.printf("Total area for input dataset: %,d ft²%n", total);
        System.out.println();
    }

    private void printCubicShapedRooms(Set<RoomRecord> roomRecordList) {
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("List of cubic shaped rooms:");
        roomRecordList.forEach(roomRecord -> System.out.printf("- Line %d: %dx%dx%d (adjusted area: %,d ft²)%n",
                roomRecord.lineNumber(), roomRecord.length(), roomRecord.width(),
                roomRecord.height(), roomRecord.customArea()));
        System.out.println();
    }

    private void printRepetitiveRooms(Map<RoomRecord, List<RoomRecord>> occurrences) {
        System.out.println(SEPARATOR);
        System.out.println();
        System.out.println("List of multiple appearances:");

        occurrences.forEach((recordKey, records) -> {
            System.out.printf("- %dx%dx%d (lines %s) %n", recordKey.length(), recordKey.width(), recordKey.height(),
                    records
                            .stream()
                            .map(roomRecord -> String.valueOf(roomRecord.lineNumber()))
                            .collect(Collectors.joining(", "))
            );
        });

        System.out.println();
    }
}
