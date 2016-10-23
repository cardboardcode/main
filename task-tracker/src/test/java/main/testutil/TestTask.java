package main.testutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.commons.util.DateUtil;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.TaskType;

/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private String message;
    private Date date1;
    private Date date2;
    private TaskType task;
    private PriorityType priority;
    private boolean isRecurring;
    private boolean isDone;
    private boolean isInferred;
    

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void setDate1(Date date) {
        this.date1 = date;
    }

    public void setDate2(Date date) {
        this.date2 = date;
    }

    public void setIsRecurring(boolean recur) {
        isRecurring = recur;
    }
    
    public void setPriority(PriorityType prior){
    	priority = prior;
    }

    @Override
    public String toString() {
        return getAsText();
    }

    public String getAddCommand() {
        StringBuilder sb = new StringBuilder();
        sb.append("add " + this.getMessage());
        
        if (date1==null|date2==null)
            return sb.toString();
        
        if (task == TaskType.EVENT) {
            sb.append(" " + getStartTimeString() + " ");
            sb.append(getEndTimeString());
        }
        else if (task == TaskType.DEADLINE) {
            sb.append(" " + getStartTimeString());
        }
        
        return sb.toString();
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Date getStartTime() {
        return date1;
    }

    @Override
    public Date getEndTime() {
        return date2;
    }

    @Override
    public Date getDeadline() {
        return date1;
    }

    @Override
    public boolean getIsFloating() {
        return this.task == TaskType.FLOATING;
    }

    @Override
    public boolean getIsEvent() {
        return this.task == TaskType.EVENT;
    }

    @Override
    public boolean getIsRecurring() {
        return isRecurring;
    }

    @Override
    public PriorityType getPriority() {
        return priority;
    }

	@Override
	public boolean getIsDeadline() {
		return this.task == TaskType.DEADLINE;
	}

    @Override
    public String getDeadlineString() {
        return DateUtil.readableDate(getDeadline());
    }

    @Override
    public String getStartTimeString() {
        return DateUtil.readableDate(getStartTime());
    }

    @Override
    public String getEndTimeString() {
        return DateUtil.readableDate(getEndTime());
    }

    @Override
    public TaskType getType() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getIsDone() {
        return isDone;
    }

    @Override
    public boolean getIsInferred() {
        // TODO Auto-generated method stub
        return false;
    }
}
