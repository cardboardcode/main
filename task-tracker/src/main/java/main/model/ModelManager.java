// adapted from addressbook level 4

package main.model;

import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.commons.core.UnmodifiableObservableList;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Logger;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.Lists;

import javafx.collections.transformation.FilteredList;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.util.DateUtil;
import main.model.ModelManager.Qualifier;
import main.model.TaskTracker;
import main.model.task.PriorityType;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;

public class ModelManager extends ComponentManager implements Model {
    private static final Logger logger = LogsCenter.getLogger(ModelManager.class);
    TaskTracker taskTracker;
    UserPrefs userPref;
    private final FilteredList<Task> filteredTasks;
    Expression current;
    
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
        Expression original = current;
        updateFilteredTaskList(Triple.of(null, DateUtil.getToday(), null));
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumTmr() {
        Expression original = current;
        updateFilteredTaskList(Triple.of(null, DateUtil.getTmr(), null));
        return getSizeAndReset(original);
    }
    
    @Override
    public int getNumEvent() {
        Expression original = current;
        updateFilteredTaskList(Triple.of(null, null, "event"));
        return getSizeAndReset(original);  
    }
    
    @Override
    public int getNumDeadline() {
        Expression original = current;
        updateFilteredTaskList(Triple.of(null, null, "deadline"));
        return getSizeAndReset(original);         
    }
    
    @Override
    public int getNumFloating() {
        Expression original = current;
        updateFilteredTaskList(Triple.of(null, null, "floating"));
        return getSizeAndReset(original); 
    }
    
    @Override 
    public int getTotalNum() {
        Expression original = current;
        updateFilteredListToShowAll();
        return getSizeAndReset(original);
    }

    private int getSizeAndReset(Expression original) {
        int num = filteredTasks.size();
        if (original == null) updateFilteredListToShowAll();
        else updateFilteredTaskList(original);
        return num;
    }
    

    //=========== Filtered Task List Accessors ===============================================================
    
    @Override
    public UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList() {
        return new UnmodifiableObservableList<>(filteredTasks);
    }

    @Override
    public void updateFilteredListToShowAll() {
        current = null;
        filteredTasks.setPredicate(null);
    }
    
    @Override
    public void updateFilteredTaskList(Triple<PriorityType, Date, String> params) {
        Expression filter = new PredicateExpression();
        
        if (params.getLeft() != null) filter.and(new PriorityQualifier(params.getLeft()));
        if (params.getMiddle() != null) filter.and(new DateQualifier(params.getMiddle()));        
        if (params.getRight() != null) filter.and(new TypeQualifier(params.getRight()));
        
        updateFilteredTaskList(filter);
        current = filter;
    }
    
    public void updateFilteredTaskList(Expression expression) {
        filteredTasks.setPredicate(expression::satisfies);
    }
    
    //========== Inner classes/interfaces used for filtering =================================================
    
    interface Expression {
        boolean satisfies(ReadOnlyTask task);
        void and(Qualifier qualifier);
        String toString();
    }

    private class PredicateExpression implements Expression {

        private List<Qualifier> qualifier = Lists.newArrayList();
        
        public PredicateExpression() {}
        
        @Override
        public void and(Qualifier qualifier) {
            this.qualifier.add(qualifier);
        }

        @Override
        public boolean satisfies(ReadOnlyTask task) {
            boolean qualify = true;
            
            for (Qualifier q : qualifier) {
                qualify = qualify && q.run(task); 
            }
            
            return qualify;
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
    
    private class PriorityQualifier implements Qualifier {
        private PriorityType priority;

        PriorityQualifier(PriorityType priority) {
            this.priority = priority;
        }

        @Override
        public boolean run(ReadOnlyTask task) {
            return priority == task.getPriority();
        }

        @Override
        public String toString() {
            return priority.toString();
        }
    }    
}
