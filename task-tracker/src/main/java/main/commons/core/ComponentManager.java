//@@author A0142686X- reused
package main.commons.core;

import javafx.collections.ObservableList;
import main.commons.events.BaseEvent;
import main.model.task.ReadOnlyTask;

/**
 * Base class for *Manager classes
 *
 * Registers the class' event handlers in eventsCenter
 */
public abstract class ComponentManager {
    protected EventsCenter eventsCenter;

    /**
     * Uses default {@link EventsCenter}
     */
    public ComponentManager(){
        this(EventsCenter.getInstance());
    }

    public ComponentManager(EventsCenter eventsCenter) {
        this.eventsCenter = eventsCenter;
        eventsCenter.registerHandler(this);
    }

    protected void raise(BaseEvent event){
        eventsCenter.post(event);
    }
    public ObservableList<ReadOnlyTask> getFilteredTaskList(){
        return null;
    }
}
