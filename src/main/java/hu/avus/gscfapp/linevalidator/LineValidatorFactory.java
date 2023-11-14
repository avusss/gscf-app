package hu.avus.gscfapp.linevalidator;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LineValidatorFactory {

    public static LineValidator createLineValidator(String input) {
        LineValidatorOption option = LineValidatorOption.fromValue(input)
                .orElse(LineValidatorOption.SMART);

        return switch (option) {
            case CASE_SENSITIVE -> new StrictCaseSensitiveLineValidator();
            case CASE_INSENSITIVE -> new LittlePermissiveCaseInsensitiveLineValidator();
            default -> new SmartLineValidator();
        };
    }

}
