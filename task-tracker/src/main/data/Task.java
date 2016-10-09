package main.data;

import java.util.Date;
import java.util.Objects;

public class Task {
    private String message;
    Date deadline;
    Date starttime;
    Date endtime;
    
    public Task(String message) {
        this.message = message;
    }
    
    public Task(String message, Date deadline) {
        this.message = message;
        this.deadline = deadline;
    }
    
    public Task(String message, Date starttime, Date endtime) {
        this.message = message;
        this.starttime = starttime;
        this.endtime = endtime;
    }
    
    public String getMessage(){
        return this.message;
    }
    
    @Override
    public boolean equals(Object other) {
        return other == this 
                || (other instanceof Task 
                && this.message.equals(((Task) other).message));
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
