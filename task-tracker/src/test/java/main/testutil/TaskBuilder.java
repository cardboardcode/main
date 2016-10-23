package main.testutil;

import java.util.Date;

import main.commons.exceptions.IllegalValueException;
import main.model.task.PriorityType;
import main.model.task.TaskType;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
        this.task.setType(TaskType.FLOATING);
    }

    public TaskBuilder withMessage(String msg) throws IllegalValueException {
        this.task.setMessage(msg);
        return this;
    }
    
    public TaskBuilder withDate(Date date) throws IllegalValueException {
        this.task.setDate1(date);
        this.task.setType(TaskType.DEADLINE);
        return this;
    }


    public TaskBuilder withDates(Date date1, Date date2) throws IllegalValueException {
        this.task.setDate1(date1);
        this.task.setDate2(date2);
        this.task.setType(TaskType.EVENT);
        return this;
    }
    
    public TaskBuilder withPriority(PriorityType priority) {
        this.task.setPriority(priority);
        return this;
    }
    
    public TestTask build() {
        return this.task;
    }

}
