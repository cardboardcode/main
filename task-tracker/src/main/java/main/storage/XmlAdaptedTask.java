//@@author A0142686X
package main.storage;

import main.commons.exceptions.IllegalValueException;
import main.model.task.TaskType;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;

import javax.xml.bind.annotation.XmlElement;

import java.util.Date;

/**
 * JAXB-friendly version of the Task.
 */
public class XmlAdaptedTask {

    @XmlElement(required = true)
    private String message;
    @XmlElement(required = false)
    private Date deadline;
    @XmlElement(required = false)
    private Date startTime;
    @XmlElement(required = false)
    private Date endTime;
    @XmlElement(required = true)
    private boolean isRecurring;
    @XmlElement(required = true)
    private PriorityType priority;
    @XmlElement(required = true)
    private TaskType type;
    @XmlElement(required = true)
    private boolean isDone;
    @XmlElement(required = true)
    private boolean isInferred;
        
    /**
     * No-arg constructor for JAXB use.
     */ 
    public XmlAdaptedTask() {}

    /**
     * Converts a given Task into this class for JAXB use.
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        message = source.getMessage();
        deadline = (Date)(source.getDeadline());
        startTime = (Date)(source.getStartTime());
        endTime = (Date)(source.getEndTime());
        isRecurring = (Boolean)(source.getIsRecurring());
        priority = (PriorityType)(source.getPriority());
        type =(TaskType)(source.getType());
        isDone = (Boolean)(source.getIsDone());
        isInferred = (Boolean)(source.getIsInferred());
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Task toModelType() throws IllegalValueException {            
        if (type == TaskType.FLOATING) {
            return new Task(message, priority).setIsInferred(isInferred).setIsRecurring(isRecurring).setDone(isDone);
        } else if (type == TaskType.EVENT) {
            return new Task(message, startTime, endTime, priority).setIsInferred(isInferred).setIsRecurring(isRecurring).setDone(isDone);
        } else {
            return new Task(message, deadline, priority).setIsInferred(isInferred).setIsRecurring(isRecurring).setDone(isDone);       
        }
    }
}
