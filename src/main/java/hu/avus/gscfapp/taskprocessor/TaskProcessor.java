package hu.avus.gscfapp.taskprocessor;

import hu.avus.gscfapp.model.RoomRecord;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Consumer;

@Setter
@Getter
public abstract class TaskProcessor<T> {

    private Consumer<T> resultHandler;

    public abstract void processLine(RoomRecord roomRecord);

    public abstract T getResults();

    public void handleResults() {
        if (resultHandler != null) {
            resultHandler.accept(getResults());
        }
    }

}
