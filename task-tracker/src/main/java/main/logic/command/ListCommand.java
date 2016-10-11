package main.logic.command;

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
            
    public ListCommand() {}
    
    public String format() {
        
    	UnmodifiableObservableList<ReadOnlyTask> list = model.getFilteredTaskList();
    	String str = "";
        for (ReadOnlyTask task : list) {
            str += task.getMessage() + "\n";
        }
        return str;
    }

	@Override
	public CommandResult execute() {
//		return new CommandResult(format());
	    model.updateFilteredListToShowAll();
	    return new CommandResult(MESSAGE_SUCCESS);
	    
	}
}
