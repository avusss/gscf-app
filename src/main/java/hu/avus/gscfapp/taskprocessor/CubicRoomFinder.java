package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;

import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

public class CubicRoomFinder extends TaskProcessor<Set<RoomRecord>> {

    private final Set<RoomRecord> results;

    public CubicRoomFinder() {
        results = new TreeSet<>(
                Comparator.comparing(RoomRecord::customArea).reversed()
                        .thenComparing(RoomRecord::lineNumber));
    }

    @Override
    public void processLine(RoomRecord roomRecord) {
        if (roomRecord.length() == roomRecord.width() && roomRecord.width() == roomRecord.height()) {
            results.add(roomRecord);
        }
    }

    @Override
    public Set<RoomRecord> getResults() {
        return results;
    }

}
