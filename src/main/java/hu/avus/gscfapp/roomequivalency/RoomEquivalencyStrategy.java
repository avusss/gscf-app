package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

public interface RoomEquivalencyStrategy {

    RoomRecord getKeyForThisRecord(RoomRecord roomRecord);

}
