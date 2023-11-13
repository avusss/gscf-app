package hu.avus.gscfapp;

import hu.avus.gscfapp.inputdatasource.FileInputDataSource;
import hu.avus.gscfapp.inputdatasource.InputDataSource;
import hu.avus.gscfapp.linevalidator.LineValidator;
import hu.avus.gscfapp.linevalidator.LineValidatorFactory;
import hu.avus.gscfapp.linevalidator.LineValidatorOption;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyOption;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategyFactory;
import hu.avus.gscfapp.taskprocessor.RoomAreaCalculatorHelper;
import org.apache.commons.cli.*;

import java.io.IOException;

public class GscfApp {

    private static final String OPTION_HELP_SHORT = "h";
    private static final String OPTION_HELP_LONG = "help";
    private static final String OPTION_VALIDATION_MODE_SHORT = "v";
    private static final String OPTION_ROOM_EQUIVALENCY_SHORT = "e";

    public static void main(String[] args) {

        GscfApp app = new GscfApp();
        Options options = app.setOptions();
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmdLine = parser.parse(options, args);
            boolean printHelp = cmdLine.hasOption(OPTION_HELP_SHORT)
                    || cmdLine.getArgList().isEmpty();

            if (printHelp) {
                app.printHelp(options);
                System.exit(0);
            }

            InputDataSource dataSource = new FileInputDataSource(cmdLine.getArgList().get(0));
            LineValidator lineValidator = LineValidatorFactory.createLineValidator(
                    cmdLine.getOptionValue(OPTION_VALIDATION_MODE_SHORT));
            RoomAreaCalculatorHelper roomAreaCalculator = new RoomAreaCalculatorHelper();

            RoomEquivalencyStrategy roomEquivalencyStrategy =
                    RoomEquivalencyStrategyFactory.createRoomEquivalencyStrategy(
                            cmdLine.getOptionValue(OPTION_ROOM_EQUIVALENCY_SHORT));

            GscfCalculator calculator = new GscfCalculator(
                    dataSource,
                    lineValidator,
                    roomAreaCalculator,
                    roomEquivalencyStrategy);
            calculator.performCalculations();

        } catch (ParseException e) {
            System.out.printf("Invalid command line input (%s)%n", e.getMessage());
            app.printHelp(options);
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
                        - %s (default) - parses any input with three numbers in it separated in any way
                        - %s - parses any input in [NUMBER]x[NUMBER]x[NUMBER] case-insensitively
                        - %s - parses any input in [NUMBER]x[NUMBER]x[NUMBER] case-sensitively
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
                        - %s (default) - room dimensions must match in any combination
                        - %s - room bae dimensions must match in any combination and heights must match
                        - %s - room dimensions must match respectively
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

}
