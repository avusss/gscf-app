package hu.avus.gscfapp.roomequivalency;

import hu.avus.gscfapp.model.RoomRecord;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * This strategy marks rooms as equivalent if both sorted rooms dimension match
 */
public class FlexibleRoomEquivalencyStrategy implements RoomEquivalencyStrategy {

    @Override
    public void addRoomToMap(Map<RoomRecord, List<RoomRecord>> map, RoomRecord roomRecord) {
        List<Integer> sortedDims = Stream.of(roomRecord.length(), roomRecord.width(), roomRecord.height())
                .sorted()
                .toList();
        RoomRecord roomRecordSortedDims = new RoomRecord(
                sortedDims.get(0),
                sortedDims.get(1),
                sortedDims.get(2),
                roomRecord.lineNumber(),
                roomRecord.customArea()
        );
        if (map.containsKey(roomRecordSortedDims)) {
            map.get(roomRecordSortedDims).add(roomRecordSortedDims);
        } else {
            map.put(roomRecordSortedDims, new ArrayList<>(List.of(roomRecord)));
        }
    }
}
