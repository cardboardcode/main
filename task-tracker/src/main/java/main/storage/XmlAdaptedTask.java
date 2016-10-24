//@@author A0142686X
package main.storage;

import main.commons.exceptions.IllegalValueException;
import main.model.task.*;

import javax.xml.bind.annotation.XmlElement;

import com.joestelmach.natty.generated.DateParser.date_return;

import java.util.Date;
import java.util.IllegalFormatCodePointException;
import java.util.ArrayList;
import java.util.List;

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
    private boolean isFloating;    
    @XmlElement(required = true)
    private boolean isEvent;
    @XmlElement(required = true)
    private boolean isDeadline;
    @XmlElement(required = true)
    private boolean isRecurring;
    @XmlElement(required = true)
    private PriorityType priority;
    @XmlElement(required = true)
    private TaskType type;
    
    
    /**
     * No-arg constructor for JAXB use.
     */ 
    public XmlAdaptedTask() {}


    /**
     * Converts a given Person into this class for JAXB use.
     *
     * @param source future changes to this will not affect the created XmlAdaptedTask
     */
    public XmlAdaptedTask(ReadOnlyTask source) {
        message = source.getMessage();
        deadline = (Date)(source.getDeadline());
        startTime =(Date)(source.getStartTime());
        endTime = (Date)(source.getEndTime());
        isFloating =(Boolean)(source.getIsFloating());
        isEvent = (Boolean)(source.getIsEvent());
        isDeadline = (Boolean)(source.getIsDeadline());
        isRecurring = (Boolean)(source.getIsRecurring());
        priority = (PriorityType)(source.getPriority());
        type = source.getType();
    }

    /**
     * Converts this jaxb-friendly adapted task object into the model's Task object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted task
     */
    public Task toModelType() throws IllegalValueException {
            
        if (type == TaskType.FLOATING) return new Task(message, priority);
        else if (type == TaskType.EVENT) return new Task(message, startTime, endTime, priority);
        else return new Task(message, deadline, priority);
        
    }
}
