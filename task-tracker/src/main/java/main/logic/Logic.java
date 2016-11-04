//@@author A0144132W
// adapted from addressbook level 4
package main.logic;

import javafx.collections.ObservableList;
import main.logic.command.CommandResult;
import main.model.task.ReadOnlyTask;

public interface Logic {
    
    /**
     * Executes the command and returns the result.
     * @param commandText The command as entered by the user.
     * @return the result of the command execution.
     */
    public CommandResult execute(String input);
    
    /** Returns the filtered list of persons */
    ObservableList<ReadOnlyTask> getFilteredTaskList();

    /** Returns the number of tasks due or happening today **/
    int getNumToday();

    /** Returns the number of tasks due or happening the next day **/
    int getNumTmr();
    
    /** Returns the number of events in the list **/
    int getNumEvent();

    /** Returns the number of tasks with deadline in the list **/
    int getNumDeadline();
    
    /** Returns the number of floating tasks in the list **/    
    int getNumFloating();

    int getTotalNum();

    int getNumOverdue();

}
