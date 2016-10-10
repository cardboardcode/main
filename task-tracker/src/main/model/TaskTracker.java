package main.model;

import java.util.List;

import javafx.collections.ObservableList;
import main.commons.core.UnmodifiableObservableList;
import main.model.model.ReadOnlyTaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList;

public class TaskTracker implements ReadOnlyTaskTracker{
    private UniqueTaskList tasks;
    
    public TaskTracker() {}
    
    /**
     * Persons and Tags are copied into this task tracker
     */
    public TaskTracker(ReadOnlyTaskTracker toBeCopied) {
        this(toBeCopied.getUniqueTaskList());
    }
    
    /**
     * Persons and Tags are copied into this task tracker
     */
    public TaskTracker(UniqueTaskList tasks) {
        resetData(tasks.getInternalList());
    }

    
    private void resetData(ObservableList<Task> internalList) {
        // TODO Auto-generated method stub
        
    }

    public UnmodifiableObservableList<ReadOnlyTask> getInternalList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UniqueTaskList getUniqueTaskList() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        // TODO Auto-generated method stub
        return null;
    }

    public void removeTask(ReadOnlyTask target) {
        // TODO Auto-generated method stub
        
    }

    public void addTask(Task task) {
        // TODO Auto-generated method stub
        
    }

    public ObservableList<Task> getTasks() {
        // TODO Auto-generated method stub
        return null;
    }
    
    public Task getTask(int index) {
        return getUniqueTaskList().getInternalList().get(index);
    }
    

}
