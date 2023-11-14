package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

import java.util.stream.IntStream;

/**
 * This strategy marks rooms as equivalent if both sorted rooms dimension match
 */
public class FlexibleRoomEquivalencyStrategy implements RoomEquivalencyStrategy {

    @Override
    public RoomRecord getKeyForThisRecord(RoomRecord source) {
        int[] sortedDims = IntStream.of(source.length(), source.width(), source.height())
                .sorted()
                .toArray();
        return RoomRecord.builder()
                .length(sortedDims[0])
                .width(sortedDims[1])
                .height(sortedDims[2])
                .lineNumber(source.lineNumber())
                .customArea(source.customArea())
                .build();
    }
}
