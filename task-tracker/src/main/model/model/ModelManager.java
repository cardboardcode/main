// adapted from addressbook level 4

package main.model.model;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javafx.collections.transformation.FilteredList;
import main.commons.core.UnmodifiableObservableList;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.util.StringUtil;
import main.model.TaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;
import main.storage.Storage;
import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;

public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    TaskTracker taskTracker;
    UserPrefs userPref;
    private final FilteredList<Task> filteredTasks;
    
    public ModelManager(TaskTracker taskTracker, UserPrefs userPref) {
        super();
        assert taskTracker != null;
        assert userPref != null;

        logger.fine("Initializing with task tracker: " + taskTracker + " and user prefs " + userPref);
        
        this.taskTracker = new TaskTracker(taskTracker);
        this.userPref = userPref;
        filteredTasks = new FilteredList<>(taskTracker.getTasks());
    }

    @Override
    public void resetData(ReadOnlyTaskTracker newData) {
        resetData(newData.getTaskList());
    }
    
    public void resetData(Collection<? extends ReadOnlyTask> newTasks) {
        setTasks(newTasks.stream().map(Task::new).collect(Collectors.toList()));
    }
    
    public void setTasks(List<Task> tasks) {
        this.taskTracker.getInternalList().setAll(tasks);
}

    @Override
    public ReadOnlyTaskTracker getTaskTracker() {
        return taskTracker;
    }

    @Override
    public void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskTracker.removeTask(target);
        indicateTaskTrackerChanged();
        
    }
    
    @Override 
    public Task getTaskfromIndex(int index) throws TaskNotFoundException {
        return taskTracker.getTask(index);       
    }
    
    /** Raises an event to indicate the model has changed */
    private void indicateTaskTrackerChanged() {
        raise(new TaskTrackerChangedEvent(taskTracker));
}

    @Override
    public void addTask(Task task) throws DuplicateTaskException {
        taskTracker.addTask(task);
        updateFilteredListToShowAll();
        indicateTaskTrackerChanged();
    }

    //=========== Filtered Task List Accessors ===============================================================
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);        
    }

    @Override
    public void updateFilteredTaskList(Set<String> keywords) {
        // TODO Auto-generated method stub
        
    }

//    @Override
//    public void updateFilteredTaskList(Set<String> keywords) {
//        updateFilteredTaskList(new PredicateExpression(new NameQualifier(keywords)));        
//    }
//
//    @Override
//    public void updateFilteredTaskList(Expression expression) {
//        filteredTasks.setPredicate(expression::satisfies);
//        
//    }
    

}
