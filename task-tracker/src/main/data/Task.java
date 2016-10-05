package main.data;

import java.util.Objects;

public class Task {
    private String message;
    
    public Task(String message) {
        this.message = message;
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
