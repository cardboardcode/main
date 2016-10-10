package main.model;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    
    public void resetData(Collection<? extends ReadOnlyTask> newTasks) {
        setTasks(newTasks.stream().map(Task::new).collect(Collectors.toList()));        
    }
    
    public void resetData(ReadOnlyTaskTracker newData) {
        resetData(newData.getTaskList());
    }
    
    public void setTasks(List<Task> tasks) {
        this.tasks.getInternalList().setAll(tasks);
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
