package main.logic.command;

import java.util.Date;
import org.apache.commons.lang3.tuple.Triple;

import main.model.task.PriorityType;

/**
 * Lists all persons in the address book to the user.
 */

public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD 
            + "Parameters: [priority] [date] [type] \n"
            + "All parameters optional and interchangeable \n"
            + "Eg: " + COMMAND_WORD + " high today";
    public static final String MESSAGE_SUCCESS = "Listed all tasks";
    
    PriorityType priority;
    Date date;
    String type;
    
    public ListCommand() {}
      
    public ListCommand(Triple<PriorityType, Date, String> parameters) {
        priority = parameters.getLeft();
        date = parameters.getMiddle();
        type = parameters.getRight();
    }

	@Override
	public CommandResult execute() {
	    model.updateFilteredListToShowAll();	    
	    model.updateFilteredTaskList(Triple.of(priority, date, type));
	    
	    return new CommandResult(MESSAGE_SUCCESS);    
	}
}
