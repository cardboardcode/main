package main.commons.events.model;

import main.commons.events.BaseEvent;
import main.model.ReadOnlyAddressBook;
import main.model.model.ReadOnlyTaskTracker;

/** Indicates the AddressBook in the model has changed*/
public class TaskTrackerChangedEvent extends BaseEvent {

    public final ReadOnlyTaskTracker data;

    public TaskTrackerChangedEvent(ReadOnlyTaskTracker data){
        this.data = data;
    }

    @Override
    public String toString() {
        return "number of persons " + data.getTaskList().size() + ", number of tags " + data.getTagList().size();
    }
}
