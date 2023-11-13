package hu.avus.gscfapp.taskprocessor;

import java.util.List;
import java.util.stream.Stream;

public class RoomAreaCalculatorHelper {

    public int calculateArea(int length, int width, int height) {
        List<Integer> sortedDims = Stream.of(length, width, height)
                .sorted()
                .toList();
        return 2 * sortedDims.get(0) * sortedDims.get(1)
                + 2 * sortedDims.get(1) * sortedDims.get(2)
                + 2 * sortedDims.get(2) * sortedDims.get(0)
                + sortedDims.get(0) * sortedDims.get(1);
    }

}
