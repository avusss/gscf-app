package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This strategy only marks rooms as equivalent if all respective dimensions match, that is:
 * room1.length = room2.length AND room1.width = room2.width AND room1.height = room2.height
 */
public class StrictRoomEquivalencyStrategy implements RoomEquivalencyStrategy {

    @Override
    public void addRoomToMap(Map<RoomRecord, List<RoomRecord>> map, RoomRecord room) {
        if (map.containsKey(room)) {
            map.get(room).add(room);
        } else {
            map.put(room, new ArrayList<>(List.of(room)));
        }

    }
}
