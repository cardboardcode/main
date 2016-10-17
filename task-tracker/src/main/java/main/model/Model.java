package main.model;

import main.model.task.Task;
import main.model.task.ReadOnlyTask;
import main.model.task.UniqueTaskList;
import main.model.task.UniqueTaskList.TaskNotFoundException;
import main.commons.core.UnmodifiableObservableList;

import java.util.Date;
//daryl
import java.util.Set;

/**
 * The API of the Model component.
 */
public interface Model {
    /** Clears existing backing model and replaces with the provided new data. */
    void resetData(ReadOnlyTaskTracker newData);

    /** Returns the AddressBook */
    ReadOnlyTaskTracker getTaskTracker();

    /** Deletes the given person. */
    void deleteTask(ReadOnlyTask target) throws UniqueTaskList.TaskNotFoundException;

    /** Adds the given person */
    void addTask(Task task) throws UniqueTaskList.DuplicateTaskException;
    
    /** Replaces the task information at the specific index */
    void editTask(int index, Task newTask) throws TaskNotFoundException, UniqueTaskList.DuplicateTaskException;
    
    /** Returns the task at the corresponding index **/
    Task getTaskfromIndex(int index) throws  UniqueTaskList.TaskNotFoundException, IndexOutOfBoundsException;

    /** Returns the filtered person list as an {@code UnmodifiableObservableList<ReadOnlyPerson>} */
    UnmodifiableObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Updates the filter of the filtered person list to show all persons */
    void updateFilteredListToShowAll();

    /** Updates the filter of the filtered task list to filter by the given date*/
    void updateFilteredTaskList(Date date);

    /** Updates the filter of the filtered task list to filter by the given priority*/
    void updateFilteredTaskList(String priority);

    /** Updates the filter of the filtered task list to filter by the given priority and date*/
    void updateFilteredTaskList(String priority, Date date);
    


}
