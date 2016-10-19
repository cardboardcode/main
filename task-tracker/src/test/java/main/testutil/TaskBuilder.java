package main.testutil;

import java.util.Date;

import main.commons.exceptions.IllegalValueException;

/**
 *
 */
public class TaskBuilder {

    private TestTask task;

    public TaskBuilder() {
        this.task = new TestTask();
    }

    public TaskBuilder withMessage(String msg) throws IllegalValueException {
        this.task.setMessage(msg);
        return this;
    }
    
    public TaskBuilder withDate(Date date) throws IllegalValueException {
        this.task.setDate1(date);
        return this;
    }


    public TaskBuilder withDates(Date date1, Date date2) throws IllegalValueException {
        this.task.setDate1(date1);
        this.task.setDate1(date2);
        return this;
    }

    public TestTask build() {
        return this.task;
    }

}
