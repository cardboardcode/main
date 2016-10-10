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

}
