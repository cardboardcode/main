package main.commons.events.model;

import main.commons.events.BaseEvent;
//import main.model.model.ReadOnlyTaskTracker;
import main.model.ReadOnlyTaskTracker;

/** Indicates the TaskTracker in the model has changed*/
public class TaskTrackerChangedEvent extends BaseEvent {

    public final ReadOnlyTaskTracker data;

    public TaskTrackerChangedEvent(ReadOnlyTaskTracker data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of tasks " + data.getTaskList().size();
    }
}
