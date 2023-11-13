package hu.avus.gscfapp.linevalidator;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum LineValidatorOption {

    SMART("smart"),
    CASE_INSENSITIVE("sci"),
    CASE_SENSITIVE("scs");

    private final String value;

    public static Optional<LineValidatorOption> fromValue(String value) {
        return Arrays.stream(LineValidatorOption.values())
                .filter(lineValidatorOption -> lineValidatorOption.getValue().equals(value))
                .findFirst();
    }
}
