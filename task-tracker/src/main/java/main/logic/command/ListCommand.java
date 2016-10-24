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
    boolean isDefault = false;
    boolean isDone = false;
    
    public ListCommand() {
        isDefault = true;
    }
      
    public ListCommand(Triple<PriorityType, Date, TaskType> parameters, boolean isDone) {
        priority = parameters.getLeft();
        date = parameters.getMiddle();
        type = parameters.getRight();
        this.isDone = isDone;
    }

	@Override
	public CommandResult execute() {

	    if (isDefault) model.updateFilteredListToShowAllPending();
	    else model.updateFilteredTaskList(Triple.of(priority, date, type), isDone);
	    
	    return new CommandResult(String.format(MESSAGE_SUCCESS, getReadableCriteria()));    
	}
	
	private String getReadableCriteria() {
	    StringBuilder readable = new StringBuilder();
	 
	    String prefix;
	    if (isDone) prefix = "completed";
	    else prefix = "pending";
	    
	    String task;
	    if (type == null) task = "tasks";
	    else if (type == TaskType.EVENT) task = "events";
        else if (type == TaskType.DEADLINE) task = "tasks with deadlines";
        else task = "floating tasks";
	    
	    if (priority == null) readable.append(prefix).append(" ").append(task);
	    else if (priority == PriorityType.LOW) readable.append(prefix).append(" low priority ").append(task);
	    else if (priority == PriorityType.HIGH) readable.append(prefix).append(" high priority ").append(task);
	    else readable.append(prefix).append(" normal priority ").append(task);

	    if (date != null) readable.append(" due ").append(DateUtil.readableDate(date));
	    
	    return readable.toString();
	    
	}
}
