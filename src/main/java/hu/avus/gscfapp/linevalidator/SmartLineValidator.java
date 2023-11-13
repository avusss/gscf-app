package hu.avus.gscfapp.linevalidator;

import java.util.regex.Pattern;

/**
 * Line validator that validates any lines that contain three numbers separated by any means
 */
public class SmartLineValidator implements LineValidator {

    private final Pattern pattern = Pattern.compile("\\D*\\d+\\D+\\d+\\D+\\d+\\D*");

    @Override
    public boolean isValid(String line) {
        return pattern.matcher(line).matches();
    }
}
