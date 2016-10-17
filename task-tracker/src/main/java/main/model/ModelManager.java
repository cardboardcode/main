// adapted from addressbook level 4

package main.model;

import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.commons.core.UnmodifiableObservableList;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.model.TaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;

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
    
    public ModelManager() {
        this(new TaskTracker(), new UserPrefs());
}

    @Override
    public void resetData(ReadOnlyTaskTracker newData) {
        taskTracker.resetData(newData);
        indicateTaskTrackerChanged();
    }
    
    public void setTasks(List<Task> tasks) {
        this.taskTracker.getUniqueTaskList().getInternalList().setAll(tasks);
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
    public void editTask(int index, Task newtask) throws TaskNotFoundException, DuplicateTaskException {
        taskTracker.editTask(index, newtask);
        updateFilteredListToShowAll();
        indicateTaskTrackerChanged();  
    }
    
    @Override 
    public Task getTaskfromIndex(int index) throws TaskNotFoundException {
        Task task;
        
        try {
            task = taskTracker.getTask(index);
        } catch (IndexOutOfBoundsException e) {
            throw new TaskNotFoundException();
        }
        
        return task;
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
//        return new UnmodifiableObservableList<>(filteredTasks);
        return new UnmodifiableObservableList<>(taskTracker.getTasks());
    }

    @Override
    public void updateFilteredListToShowAll() {
        filteredTasks.setPredicate(null);        
    }

    public void updateFilteredTaskList(Date date)) {
        updateFilteredTaskList(new PredicateExpression(new DateQualifier(date)));        
    }

    public void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
        
    }
    
    //========== Inner classes/interfaces used for filtering =================================================

    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private final Qualifier qualifier;

        PredicateExpression(Qualifier qualifier) {
            this.qualifier = qualifier;
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            return qualifier.run(task);
        }

        @Override
        public String toString() {
            return qualifier.toString();
        }
    }       

    interface Qualifier {
        boolean run(ReadOnlyTask task);
        String toString();
    }
//
    private class DateQualifier implements Qualifier {
        private Date date;

        DateQualifier(Date date) {
            this.date = date;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
//            return date.stream()
//                    .filter(keyword -> StringUtil.containsWordIgnoreCase(person.getName().fullName, keyword))
//                    .findAny()
//                    .isPresent();
            return (date.equals(task.getDeadline()) || date.equals(task.getEndTime()) || date.equals(task.getStartTime()));
        }

        @Override
        public String toString() {
            return "name=" + String.join(", ", nameKeyWords);
        }
}

}
