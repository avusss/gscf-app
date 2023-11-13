package hu.avus.gscfapp.roomequivalency;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RoomEquivalencyStrategyFactory {

    public static RoomEquivalencyStrategy createRoomEquivalencyStrategy(String input) {
        RoomEquivalencyOption option = RoomEquivalencyOption.fromValue(input)
                .orElse(RoomEquivalencyOption.FLEXIBLE);

        return switch (option) {
            case STRICT -> new StrictRoomEquivalencyStrategy();
            case BASE -> new RoomBaseEquivalencyStrategy();
            default -> new FlexibleRoomEquivalencyStrategy();
        };
    }

}
