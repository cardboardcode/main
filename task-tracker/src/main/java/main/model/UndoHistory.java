package main.model;

import java.util.ArrayList;

import edu.emory.mathcs.backport.java.util.Arrays;
import main.model.task.ReadOnlyTask;

public class UndoHistory {
    
    private int ID;
    private ArrayList<ReadOnlyTask> tasks;
    
    public UndoHistory(int ID, ReadOnlyTask... tasks){
        this.ID = ID;
        this.tasks = (ArrayList<ReadOnlyTask>)Arrays.asList(tasks);       
    }
    
    public int getID(){
        return ID;
    }
    
    public ArrayList<ReadOnlyTask> getTasks(){
        return tasks;
    }

}
