//@@author A0142686X
package main.model;

import java.util.ArrayList;
import java.util.Collection;

import edu.emory.mathcs.backport.java.util.Arrays;
import main.model.task.Task;

public class UndoHistory {
    /**
     * Maintains an ArrayList of tasks changed in Tasktracker at every command.
     * 
     */
    
    private int ID;
    private ArrayList<Task> tasks;
    
    public UndoHistory(int ID, Task... tasks) {
        this.ID = ID;
        Collection<Task> collection = Arrays.asList(tasks);
        this.tasks = new ArrayList<Task>(collection);       
    }
    
    
    public int getID() {
        return ID;
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }   
    
}
