//@@author A0142686X
package main.storage;

import main.commons.exceptions.IllegalValueException;
import main.model.ReadOnlyTaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.UniqueTaskList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * An Immutable TaskTracker that is serializable to XML format
 */
@XmlRootElement(name = "tasktracker")
public class XmlSerializableTaskTracker implements ReadOnlyTaskTracker {

    @XmlElement
    private List<XmlAdaptedTask> tasks;
    {
        tasks = new ArrayList<>();
    }

    /**
     * Empty constructor required for marshalling
     */
    public XmlSerializableTaskTracker() {}

    /**
     * Conversion
     */
    public XmlSerializableTaskTracker(ReadOnlyTaskTracker src) {
        tasks.addAll(src.getTaskList().stream().map(XmlAdaptedTask::new).collect(Collectors.toList()));
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        UniqueTaskList lists = new UniqueTaskList();
        for (XmlAdaptedTask t : tasks) {
            try {
                lists.add(t.toModelType());
            } catch (IllegalValueException e) {
                e.printStackTrace();
            }
        }
        return lists;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return tasks.stream().map(t -> {
            try {
                return t.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                return null;
            }
        }).collect(Collectors.toCollection(ArrayList::new));
    }
}
