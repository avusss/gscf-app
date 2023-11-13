package hu.avus.gscfapp.roomequivalency;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Optional;

@RequiredArgsConstructor
@Getter
public enum RoomEquivalencyOption {

    FLEXIBLE("flex"),
    BASE("base"),
    STRICT("strict");

    private final String value;

    public static Optional<RoomEquivalencyOption> fromValue(String value) {
        return Arrays.stream(RoomEquivalencyOption.values())
                .filter(lineValidatorOption -> lineValidatorOption.getValue().equals(value))
                .findFirst();
    }

}
