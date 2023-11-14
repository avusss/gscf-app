package hu.avus.gscfapp;

import hu.avus.gscfapp.inputdatasource.FileInputDataSource;
import hu.avus.gscfapp.inputdatasource.InputDataSource;
import hu.avus.gscfapp.linevalidator.LineValidator;
import hu.avus.gscfapp.linevalidator.LineValidatorFactory;
import hu.avus.gscfapp.linevalidator.LineValidatorOption;
import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyOption;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategyFactory;
import hu.avus.gscfapp.taskprocessor.*;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class GscfApp {

    public static final String SEPARATOR =
            "--------------------------------------------------------------------------------";

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_HELP_LONG = "help";
    private static final String OPTION_VALIDATION_MODE_SHORT = "v";
    private static final String OPTION_ROOM_EQUIVALENCY_SHORT = "e";

    public static void main(String[] args) {
        GscfApp app = new GscfApp();
        app.bootstrap(args);
    }

    private void bootstrap(String[] args) {
        Options options = setOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmdLine = parser.parse(options, args);
            boolean printHelp = cmdLine.hasOption(OPTION_HELP_SHORT)
                    || cmdLine.getArgList().isEmpty();

            if (printHelp) {
                printHelp(options);
                System.exit(0);
            }

            InputDataSource dataSource = new FileInputDataSource(cmdLine.getArgList().get(0));
            LineValidator lineValidator = LineValidatorFactory.createLineValidator(
                    cmdLine.getOptionValue(OPTION_VALIDATION_MODE_SHORT));
            RoomAreaCalculatorHelper roomAreaCalculator = new RoomAreaCalculatorHelper();

            RoomEquivalencyStrategy roomEquivalencyStrategy =
                    RoomEquivalencyStrategyFactory.createRoomEquivalencyStrategy(
                            cmdLine.getOptionValue(OPTION_ROOM_EQUIVALENCY_SHORT));

            List<TaskProcessor<?>> taskProcessors = List.of(
                    new TotalAreaCalculator().setResultHandler(this::printTotalResult),
                    new CubicRoomFinder().setResultHandler(this::printCubicShapedRooms),
                    new RepetitionFinder(roomEquivalencyStrategy).setResultHandler(this::printRepetitiveRooms)
            );
            GscfCalculator calculator = new GscfCalculator(
                    dataSource,
                    lineValidator,
                    roomAreaCalculator,
                    taskProcessors);
            calculator.performCalculations();

            printLineReport(calculator.getAllLines(), calculator.getInvalidLines());
            calculator.handleResults();

        } catch (ParseException e) {
            System.out.printf("Invalid command line input (%s)%n", e.getMessage());
            printHelp(options);
            System.exit(1);
        } catch (IOException e) {
            System.out.printf("Input file could not be parsed (%s) %n", e.getMessage());
            System.exit(1);
        }
    }

    private void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar <JAR_PATH> <INPUT_FILE_PATH>", options, true);
    }

    private Options setOptions() {
        Options options = new Options();
        options.addOption(Option.builder()
                .option(OPTION_HELP_SHORT)
                .longOpt(OPTION_HELP_LONG)
                .desc("Prints this screen")
                .build());
        options.addOption(Option.builder()
                .option(OPTION_VALIDATION_MODE_SHORT)
                .desc("""
                        Sets input line validation strategy, options:
                        - %s (default) - Parses any input with three numbers in it separated in any way (eg. Lorem 12 ipsum 34 dolor 45 sit amet parses to 12x34x45)
                        - %s - Parses any input with a [NUMBER]X[NUMBER]X[NUMBER] format case-insensitively (eg. 12X34X45)
                        - %s -  Only parses inputs with a [NUMBER]x[NUMBER]x[NUMBER] format case-sensitively (eg. 12x34x45)
                        """.formatted(
                        LineValidatorOption.SMART.getValue(),
                        LineValidatorOption.CASE_INSENSITIVE.getValue(),
                        LineValidatorOption.CASE_SENSITIVE.getValue()
                ))
                .optionalArg(true)
                .argName(""
                        .concat(LineValidatorOption.SMART.getValue().concat("|"))
                        .concat(LineValidatorOption.CASE_INSENSITIVE.getValue().concat("|"))
                        .concat(LineValidatorOption.CASE_SENSITIVE.getValue())
                )
                .build());
        options.addOption(Option.builder()
                .option(OPTION_ROOM_EQUIVALENCY_SHORT)
                .desc("""
                        Sets room equivalency strategy, options:
                        - %s (default) - Considers two rooms equivalent if the room dimensions match in any combination (eg. 12x34x45 is equivalent to 45x34x12, 12x34x45 is equivalent to 34x12x45)
                        - %s - Considers two rooms equivalent if the room base dimensions (length and width) match in any combination (eg. 12x34x45 is equivalent to 34x12x45, but NOT equivalent to 45x34x12)
                        - %s - Considers two rooms equivalent if all room dimensions match respectively (eg. length matches with length, width matches with width, height matches with height)
                        """.formatted(
                        RoomEquivalencyOption.FLEXIBLE.getValue(),
                        RoomEquivalencyOption.BASE.getValue(),
                        RoomEquivalencyOption.STRICT.getValue()
                ))
                .optionalArg(true)
                .argName(""
                        .concat(RoomEquivalencyOption.FLEXIBLE.getValue().concat("|"))
                        .concat(RoomEquivalencyOption.BASE.getValue().concat("|"))
                        .concat(RoomEquivalencyOption.STRICT.getValue())
                )
                .build());
        return options;
    }

    private void printLineReport(int allLines, int invalidLines) {
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
            System.out.printf("- %dx%dx%d (lines %s) %n",
                    records.get(0).length(), records.get(0).width(), records.get(0).height(),
                    records
                            .stream()
                            .map(roomRecord -> String.valueOf(roomRecord.lineNumber()))
                            .collect(Collectors.joining(", "))
            );
        });

        System.out.println();
    }

}
