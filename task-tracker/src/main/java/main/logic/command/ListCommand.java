package main.logic.command;

import java.util.Date;
import java.util.List;

import main.commons.core.UnmodifiableObservableList;
import main.model.task.ReadOnlyTask;

/**
 * Lists all persons in the address book to the user.
 */

public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Listed all tasks";
    
    String priority;
    Date date;
            
    public ListCommand() {}
    
    public ListCommand(String priority) {
        this.priority = priority;
    }
    
    public ListCommand(Date date) {
        this.date = date;
    }
    
    public ListCommand(String priority, Date date) {
        this.priority = priority;
        this.date = date;
    }

	@Override
	public CommandResult execute() {
	    if (priority == null && date == null) model.updateFilteredListToShowAll();
	    else if (priority == null) model.updateFilteredTaskList(date);
	    else if (date == null) model.updateFilteredTaskList(priority);
	    else model.updateFilteredTaskList(priority,date);
	    
	    return new CommandResult(MESSAGE_SUCCESS);    
	}
}
