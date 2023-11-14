package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

/**
 * This strategy only marks rooms as equivalent if all respective dimensions match, that is:
 * room1.length = room2.length AND room1.width = room2.width AND room1.height = room2.height
 */
public class StrictRoomEquivalencyStrategy implements RoomEquivalencyStrategy {

    @Override
    public RoomRecord getKeyForThisRecord(RoomRecord roomRecord) {
        return roomRecord;
    }
}
