package hu.avus.gscfapp.linevalidator;

import java.util.regex.Pattern;

/**
 * Line validator that only validates an input line if it has a
 * [NUMBER]x[NUMBER]x[NUMBER]
 * format (case-insensitive)
 */
public class LittlePermissiveCaseInsensitiveLineValidator implements LineValidator {

    private final Pattern pattern = Pattern.compile("\\d+x\\d+x\\d+", Pattern.CASE_INSENSITIVE);

    @Override
    public boolean isValid(String line) {
        return pattern.matcher(line).matches();
    }

}
