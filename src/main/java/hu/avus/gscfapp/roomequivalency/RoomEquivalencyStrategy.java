package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

import java.util.List;
import java.util.Map;

public interface RoomEquivalencyStrategy {

    RoomRecord getKeyForThisRecord(RoomRecord roomRecord);

}
