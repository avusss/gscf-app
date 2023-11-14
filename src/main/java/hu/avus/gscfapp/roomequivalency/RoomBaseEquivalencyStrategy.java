package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

/**
 * This strategy only marks rooms as equivalent if the dimensions of the room bases match (in any combination)
 * and the heights match, that is:
 * (room1.length = room2.length OR room1.length = room2.width)
 * AND (room1.width = room2.width OR room1.width = room2.length)
 * AND room1.height = room2.height
 */
public class RoomBaseEquivalencyStrategy implements RoomEquivalencyStrategy {

    @Override
    public RoomRecord getKeyForThisRecord(RoomRecord source) {
        return RoomRecord.builder()
                .length(Math.min(source.length(), source.width()))
                .width(Math.max(source.length(), source.width()))
                .height(source.height())
                .lineNumber(source.lineNumber())
                .customArea(source.customArea())
                .build();
    }
}
