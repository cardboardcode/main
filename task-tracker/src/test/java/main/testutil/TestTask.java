package main.testutil;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;

/**
 * A mutable task object. For testing only.
 */
public class TestTask implements ReadOnlyTask {

    private String message;
    private Date date1;
    private Date date2;
    private boolean isFloating;
    private boolean isEvent;
    private boolean isDeadline;
    private PriorityType priority;
    private boolean isRecurring;
    

    public void setMessage(String msg) {
        this.message = msg;
    }

    public void setDate1(Date date) {
        this.date1 = date;
    }

    public void setDate2(Date date) {
        this.date2 = date;
    }
    
    public void setIsFloating(boolean floating) {
        isFloating = floating;
    }
    
    public void setIsEvent(boolean event) {
        isEvent = event;
    }
    
    public void setIsDeadline(boolean deadline){
    	isDeadline = deadline;
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
        if (isEvent) {
            sb.append(" " + readableDate(date1) + " ");
            sb.append(readableDate(date2));
        }
        else if (!isEvent && !isFloating) {
            sb.append(" " + readableDate(date1));
        }
        return sb.toString();
    }
    
    private String readableDate(Date date) {
        DateFormat df = new SimpleDateFormat("dd MMM h-mm a");
        String dateString = df.format(date);
        return dateString; 
        
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
        return isFloating;
    }

    @Override
    public boolean getIsEvent() {
        return isEvent;
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
		return isDeadline;
	}
}
