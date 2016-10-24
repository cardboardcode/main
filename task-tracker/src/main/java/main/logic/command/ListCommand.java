//@@author A0144132W
package main.logic.command;

import java.util.Date;
import org.apache.commons.lang3.tuple.Triple;

import main.commons.util.DateUtil;
import main.model.task.PriorityType;
import main.model.task.TaskType;

/**
 * Lists certain tasks in the task tracker to the user.
 */

public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD 
            + "Parameters: [priority] [date] [type] \n"
            + "All parameters optional and interchangeable \n"
            + "Eg: " + COMMAND_WORD + " high today";
    public static final String MESSAGE_SUCCESS = "Listed all %1$s";
    
    PriorityType priority;
    Date date;
    TaskType type;
    boolean toShowAll = false;
    
    public ListCommand() {
        toShowAll = true;
    }
      
    public ListCommand(Triple<PriorityType, Date, TaskType> parameters) {
        priority = parameters.getLeft();
        date = parameters.getMiddle();
        type = parameters.getRight();
    }

	@Override
	public CommandResult execute() {
	       
	    model.updateFilteredListToShowAllPending();
	    if (!toShowAll) model.updateFilteredTaskList(Triple.of(priority, date, type));
	    
	    return new CommandResult(String.format(MESSAGE_SUCCESS, getReadableCriteria()));    
	}
	
	private String getReadableCriteria() {
	    StringBuilder readable = new StringBuilder();
	    
	    String task;
	    if (type == null) task = "tasks";
	    else if (type == TaskType.EVENT) task = "events";
        else if (type == TaskType.DEADLINE) task = "tasks with deadlines";
        else task = "floating tasks";
	    
	    if (priority == null) readable.append(task);
	    else if (priority == PriorityType.LOW) readable.append("low priority ").append(task);
	    else if (priority == PriorityType.HIGH) readable.append("high priority ").append(task);
	    else readable.append("normal priority ").append(task);

	    if (date != null) readable.append(" due ").append(DateUtil.readableDate(date));
	    
	    return readable.toString();
	    
	}
}
