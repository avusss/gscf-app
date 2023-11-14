package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import hu.avus.gscfapp.roomequivalency.RoomEquivalencyStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class RepetitionFinder extends TaskProcessor<Map<RoomRecord, List<RoomRecord>>> {

    private final RoomEquivalencyStrategy equivalencyStrategy;

    Map<RoomRecord, List<RoomRecord>> resultMap;

    public RepetitionFinder(RoomEquivalencyStrategy equivalencyStrategy) {
        this.equivalencyStrategy = equivalencyStrategy;
        resultMap = new TreeMap<>(RoomRecord.DEFAULT_COMPARATOR);
    }

    @Override
    public void processLine(RoomRecord roomRecord) {
        RoomRecord key = equivalencyStrategy.getKeyForThisRecord(roomRecord);
        if (resultMap.containsKey(key)) {
            resultMap.get(key).add(roomRecord);
        } else {
            resultMap.put(key, new ArrayList<>(List.of(roomRecord)));
        }
    }

    @Override
    public Map<RoomRecord, List<RoomRecord>> getResults() {
        Map<RoomRecord, List<RoomRecord>> filteredMap = new TreeMap<>(RoomRecord.DEFAULT_COMPARATOR);
        resultMap.forEach((recordKey, records) -> {
            if (records.size() > 1) {
                filteredMap.put(records.get(0), records);
            }
        });
        return filteredMap;
    }

    public Map<RoomRecord, List<RoomRecord>> getOriginalResultMap() {
        Map<RoomRecord, List<RoomRecord>> copiedMap = new TreeMap<>(RoomRecord.DEFAULT_COMPARATOR);
        resultMap.forEach((recordKey, records) -> {
            copiedMap.put(records.get(0), records);
        });
        return copiedMap;
    }

}
