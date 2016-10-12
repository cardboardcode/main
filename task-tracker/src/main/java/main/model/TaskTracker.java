package main.model;

import main.commons.core.LogsCenter;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.ObservableList;
import main.model.ReadOnlyTaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList;
import main.model.task.UniqueTaskList.DuplicateTaskException;

public class TaskTracker implements ReadOnlyTaskTracker{
    private static final Logger logger = LogsCenter.getLogger(TaskTracker.class);

    private UniqueTaskList tasks = new UniqueTaskList();
    
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


    @Override
    public UniqueTaskList getUniqueTaskList() {
        return this.tasks;
    }

    @Override
    public List<ReadOnlyTask> getTaskList() {
        return Collections.unmodifiableList(tasks.getInternalList());
    }

    public void removeTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException {
        if (!tasks.remove(target)) {
            throw new UniqueTaskList.TaskNotFoundException();
        }
    }

    public void addTask(Task task) throws DuplicateTaskException {
        tasks.add(task);        
    }

    public void editTask(int index, Task newtask) throws DuplicateTaskException {
        tasks.replace(index, newtask);
    }   
    
    public ObservableList<Task> getTasks() {
        return tasks.getInternalList();
    }
    
    public Task getTask(int index) {
        return getUniqueTaskList().getInternalList().get(index);
    }
    
    @Override
    public String toString() {
        return tasks.getInternalList().size() + " tasks";
      
        // TODO: refine later
//        String str = "";
//        Iterator<Task> iterate = tasks.iterator();
//        while (iterate.hasNext()) {
//            Task element = iterate.next();
//            str += element + " ";
//        }
//        return str;
        
    }
   
}
