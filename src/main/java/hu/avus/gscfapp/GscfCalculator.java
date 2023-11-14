package hu.avus.gscfapp;

import hu.avus.gscfapp.inputdatasource.InputDataSource;
import hu.avus.gscfapp.linevalidator.LineValidator;
import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.taskprocessor.RoomAreaCalculatorHelper;
import hu.avus.gscfapp.taskprocessor.TaskProcessor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class GscfCalculator {

    private final Iterator<String> iterator;
    private final LineValidator validator;
    private final RoomAreaCalculatorHelper areaCalculator;
    private final List<TaskProcessor<?>> taskProcessors;

    @Getter
    private int invalidLines = 0;
    @Getter
    private int allLines = 0;

    public GscfCalculator(
            InputDataSource dataSource,
            LineValidator validator,
            RoomAreaCalculatorHelper roomAreaCalculator,
            List<TaskProcessor<?>> taskProcessors) {
        this.iterator = dataSource.iterator();
        this.validator = validator;
        this.areaCalculator = roomAreaCalculator;
        this.taskProcessors = taskProcessors;
    }

    public void performCalculations() {
        while (iterator.hasNext()) {
            String line = iterator.next();
            processLine(line);
        }
    }

    public void handleResults() {
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
            RoomRecord roomRecord = RoomRecord.builder()
                    .length(dims.get(0))
                    .width(dims.get(1))
                    .height(dims.get(2))
                    .lineNumber(allLines)
                    .customArea(areaCalculator.calculateArea(dims.get(0), dims.get(1), dims.get(2)))
                    .build();
                    areaCalculator.calculateArea(dims.get(0), dims.get(1), dims.get(2));
            taskProcessors.forEach(taskProcessor -> taskProcessor.processLine(roomRecord));
        } else {
            invalidLines++;
        }
    }

}
