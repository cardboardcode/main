// adapted from addressbook level 4

package main.model;

import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.commons.core.UnmodifiableObservableList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.transformation.FilteredList;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.util.DateUtil;
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
    PredicateExpression current;
    
    public ModelManager(TaskTracker taskTracker, UserPrefs userPref) {
        super();
        assert taskTracker != null;
        assert userPref != null;

        logger.fine("Initializing with task tracker: " + taskTracker + " and user prefs " + userPref);
        
        this.taskTracker = new TaskTracker(taskTracker);
        this.userPref = userPref;
        filteredTasks = new FilteredList<>(this.taskTracker.getTasks());
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
    public synchronized void deleteTask(ReadOnlyTask target) throws TaskNotFoundException {
        taskTracker.removeTask(target);
        indicateTaskTrackerChanged();
        
    }
    
    @Override
    public synchronized void editTask(int index, Task newtask) throws TaskNotFoundException, DuplicateTaskException {
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
    public synchronized void addTask(Task task) throws DuplicateTaskException {
        taskTracker.addTask(task);
        updateFilteredListToShowAll();
        indicateTaskTrackerChanged();
    }
    
    //=========== User Friendly Accessors ===================================================================
    @Override
    public int getNumToday() {
        PredicateExpression original = current;
        Calendar cal = Calendar.getInstance(); //TODO shift to DateUtil
        updateFilteredTaskList(cal.getTime());
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumTmr() {
        PredicateExpression original = current;
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, 1);  //TODO check if at 31 will error
        updateFilteredTaskList(cal.getTime());
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumEvent() {
        PredicateExpression original = current;
        updateFilteredTaskList("event");
        return getSizeAndReset(original);  
    }
    
    @Override
    public int getNumDeadline() {
        PredicateExpression original = current;
        updateFilteredTaskList("deadline");
        return getSizeAndReset(original);         
    }
    
    @Override
    public int getNumFloating() {
        PredicateExpression original = current;
        updateFilteredTaskList("floating");
        return getSizeAndReset(original); 
    }
    
    @Override 
    public int getTotalNum() {
        PredicateExpression original = current;
        updateFilteredListToShowAll();
        return getSizeAndReset(original);
    }

    private int getSizeAndReset(PredicateExpression original) {
        int num = filteredTasks.size();
        if (original == null) updateFilteredListToShowAll();
        else updateFilteredTaskList(original);
        return num;
    }
    

    //=========== Filtered Task List Accessors ===============================================================
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
//        return new UnmodifiableObservableList<>(taskTracker.getTasks());
    }

    @Override
    public void updateFilteredListToShowAll() {
        current = null;
        filteredTasks.setPredicate(null);
    }

    @Override
    public void updateFilteredTaskList(Date date) {
        current = new PredicateExpression(new DateQualifier(date));
        updateFilteredTaskList(current);
        
    }
    
    @Override
    public void updateFilteredTaskList(String type) {
        current = new PredicateExpression(new TypeQualifier(type.trim()));
        updateFilteredTaskList(current);        
    }
    
    
//    //TODO
//    @Override
//    public void updateFilteredTaskList(String priority) {
//        updateFilteredListToShowAll();
////        updateFilteredTaskList(new PredicateExpression(new DateQualifier(date)));        
//    }
    
    //TODO
    @Override
    public void updateFilteredTaskList(String priority, Date date) {
        updateFilteredListToShowAll();
//        updateFilteredTaskList(new PredicateExpression(new DateQualifier(date)));        
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
//            return ((compareDate(date, task.getDeadline())) || (compareDate(date, task.getEndTime())) || (compareDate(date, task.getStartTime())));
            if (task.getIsEvent()) return DateUtil.dateWithin(task.getStartTime(), task.getEndTime(), date);
            else if (!task.getIsFloating() && !task.getIsEvent()) return DateUtil.areSameDay(date, task.getDeadline()); 
            else return false;
        }


        @Override
        public String toString() {
            return ((new SimpleDateFormat("dd MMM")).format(this.date));
        }
    }
    
    private class TypeQualifier implements Qualifier {
        private String type;

        TypeQualifier(String type) {
            this.type = type;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            type = type.toLowerCase();
            if (type.equals("floating")) return (task.getIsFloating());
            else if (type.equals("event")) return (task.getIsEvent());
            else if (type.equals("deadline")) return (!task.getIsFloating() && !task.getIsEvent());
            else return false;
        }

        @Override
        public String toString() {
            return type;
        }
    }
}
