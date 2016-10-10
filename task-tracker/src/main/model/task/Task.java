package main.model.task;

import java.util.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Objects;

public class Task implements ReadOnlyTask {
    private String message;
    Date deadline;
    Date startTime;
    Date endTime;
    private boolean isFloating;
    
    public Task(String message) {
        this.message = message;
        this.isFloating = true; 
    }
    
    public Task(String message, Date deadline) {
        this.message = message;
        this.deadline = deadline;
        this.isFloating = false;
    }
    
    public Task(String message, Date startTime, Date endTime, Date deadline) {
        this.message = message;
        this.deadline = deadline;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isFloating = false;
    }
    //getters
    @Override
    public String getMessage(){
        return this.message;
    }
    @Override
    public Date getStartTime(){
    	return this.startTime;
    }
    
    public String getStartTimeString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.startTime);
		return dateString; 
	}
    
    @Override
    public Date getEndTime(){
    	return this.endTime;
    }
    
    public String getEndTimeString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.endTime);
		return dateString;
	}
    
    @Override
    public Date getDeadline(){
    	return this.deadline;
    }
    
    public String getDeadlineString() {
		String dateString = "";
		DateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm");
		dateString = df.format(this.deadline);
		return dateString;	
	}
    @Override
    public boolean getIsFloating(){
    	return this.isFloating;
    }
    //setters
    public void setMessage(String message){
    	this.message = message;
    }
    public void setStartTime(Date startTime){
    	this.startTime = startTime;
    } 
    
    public void setEndTime(Date endTime){
    	this.endTime = endTime;
    }
       
    public void setDeadline(Date deadline){
    	this.deadline = deadline;
    }  
    
    public void setIsFloating(boolean isFloating){
    	this.isFloating = isFloating;
    }
     
    @Override
    public boolean equals(Object other) {
    	if(this.isFloating && this.message.equals(((Task) other).message)){
    			return other == this 
                    || (other instanceof Task);
        }
    	if(!this.isFloating && this.message.equals(((Task) other).message)
    		&& this.startTime.equals(((Task) other).startTime)
    		&& this.endTime.equals(((Task) other).endTime)){
    			return other == this 
                    || (other instanceof Task);       
    	}
    	if(!this.isFloating && this.message.equals(((Task) other).message)
    		&& this.deadline.equals(((Task) other).deadline)){
    			return other == this 
    				|| (other instanceof Task); 
    	}
    }
       
    @Override
    public int hashCode() {
        return Objects.hash(message);
    }
    
    @Override
    public String toString() {
        return getMessage();
    }


}
