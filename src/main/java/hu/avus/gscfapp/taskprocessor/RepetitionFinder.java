package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RepetitionFinder extends TaskProcessor<Map<RoomRecord, List<RoomRecord>>> {

    private static final Comparator<RoomRecord> COMPARATOR = Comparator.comparing(RoomRecord::length)
            .thenComparing(RoomRecord::width)
            .thenComparing(RoomRecord::height);

    private final RoomEquivalencyStrategy equivalencyStrategy;

    Map<RoomRecord, List<RoomRecord>> resultMap;

    public RepetitionFinder(RoomEquivalencyStrategy equivalencyStrategy) {
        this.equivalencyStrategy = equivalencyStrategy;
        resultMap = new TreeMap<>(COMPARATOR);
    }

    @Override
    public void processLine(RoomRecord roomRecord) {
        equivalencyStrategy.addRoomToMap(resultMap, roomRecord);
    }

    @Override
    public Map<RoomRecord, List<RoomRecord>> getResults() {
        Map<RoomRecord, List<RoomRecord>> filteredMap = new TreeMap<>(COMPARATOR);
        resultMap.forEach((recordKey, records) -> {
            if (records.size() > 1) {
                filteredMap.put(records.get(0), records);
            }
        });
        return filteredMap;
    }
}
