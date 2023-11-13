package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * This strategy only marks rooms as equivalent if the dimensions of the room bases match (in any combination)
 * and the heights match, that is:
 * (room1.length = room2.length OR room1.length = room2.width)
 * AND (room1.width = room2.width OR room1.width = room2.length)
 * AND room1.height = room2.height
 */
public class RoomBaseEquivalencyStrategy implements RoomEquivalencyStrategy {
    @Override
    public void addRoomToMap(Map<RoomRecord, List<RoomRecord>> map, RoomRecord roomRecord) {
        List<Integer> sortedBaseDims = Stream.of(roomRecord.length(), roomRecord.width())
                .sorted()
                .toList();
        RoomRecord roomRecordSortedBaseDims = new RoomRecord(
                sortedBaseDims.get(0),
                sortedBaseDims.get(1),
                roomRecord.height(),
                roomRecord.lineNumber(),
                roomRecord.customArea()
                );
        if (map.containsKey(roomRecordSortedBaseDims)) {
            map.get(roomRecordSortedBaseDims).add(roomRecordSortedBaseDims);
        } else {
            map.put(roomRecordSortedBaseDims, new ArrayList<>(List.of(roomRecord)));
        }
    }
}
