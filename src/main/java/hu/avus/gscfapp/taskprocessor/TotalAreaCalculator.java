package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TotalAreaCalculator extends TaskProcessor<Long> {

    private long totalArea = 0;

    @Override
    public void processLine(RoomRecord roomRecord) {
        totalArea += roomRecord.customArea();
    }

    @Override
    public Long getResults() {
        return totalArea;
    }
}
