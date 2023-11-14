package hu.avus.gscfapp.model;

import lombok.Builder;

import java.util.Comparator;

@Builder
public record RoomRecord(
        int length,
        int width,
        int height,
        int lineNumber,
        int customArea
) {

    public static Comparator<RoomRecord> DEFAULT_COMPARATOR = Comparator.comparing(RoomRecord::length)
            .thenComparing(RoomRecord::width)
            .thenComparing(RoomRecord::height);

}
