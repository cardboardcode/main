//@@author A0142686X
package main.model;

import java.util.ArrayList;
import java.util.Collection;
import edu.emory.mathcs.backport.java.util.Arrays;
import main.model.task.Task;

/**
 * An ArrayList of tasks changed in Tasktracker at every command.
 * 
 */
public class UndoHistory {        
    private int ID;
    private ArrayList<Task> tasks;
    
    public UndoHistory(int ID, Task... tasks) {
        this.ID = ID;
        Collection<Task> collection = Arrays.asList(tasks);
        this.tasks = new ArrayList<Task>(collection);       
    }
    
    /**
     * Returns the command ID of executed command
     */
    public int getID() {
        return this.ID;
    }
    
    public ArrayList<Task> getTasks() {
        return this.tasks;
    }       
}
