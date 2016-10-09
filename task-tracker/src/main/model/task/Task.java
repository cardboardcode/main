package main.model.task;

import main.commons.util.CollectionUtil;



import java.util.Objects;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Task implements ReadOnlyTask {
	private int taskID;
    private Message message;
    private Time time;
    private boolean isFloating;
    
    //constructor for floating task
    public Task(int taskID, Message message){
    	this.taskID = taskID;
    	this.message = message;
    	this.isFloating = true; 
    }
    /**
     * Every field must be present and not null.
     */
    public Task(int taskID, Message message, Time time, boolean isFloating) {
        assert !CollectionUtil.isAnyNull(taskID, message, time, isFloating);
        this.taskID = taskID;
        this.message = message;
        this.time = time;       
        this.isFloating = isFloating;
        
    }

    /**
     * Copy constructor.
     */
    public Task(ReadOnlyTask source) {
        this(source.getTaskID(), source.getMessage(), source.getTime(), source.getIsFloating());
    }
    @Override
    public int getTaskID(){
    	return taskID;
    }
    
    public void setTaskID(int taskID){
    	this.taskID = taskID;
    }
    @Override
    public Message getMessage() {
        return message;
    }
    
    public void setMessage(Message message){
    	this.message = message ;
    }

    @Override
    public Time getTime() {
        return time;
    }
    
    public void setTime(Time time){
    	this.time = time;
    }

    @Override
    public boolean getIsFloating() {
        return getIsFloating();
    }
   
    public void setIsFloating(){
    	isFloating = true;
    }
    
    public void setIsNotFloating(){
    	isFloating = false;
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof ReadOnlyTask // instanceof handles nulls
                && this.isSameStateAs((ReadOnlyTask) other));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(taskID, message, time, isFloating);
    }

    @Override
    public String toString() {
        return getAsText();
    }

}
