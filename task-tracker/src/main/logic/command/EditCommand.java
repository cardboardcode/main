package main.logic.command;

import java.util.Date;

import main.commons.exceptions.IllegalValueException;
import main.model.task.Task;
import main.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Edits an existing task in task-tracker
 * @author 
 *
 */
public class EditCommand extends Command {
public static final String COMMAND_WORD = "edit";
	
	public static final String MESSAGE_USAGE = COMMAND_WORD + ":/n" + "Edits a task in the Task-Tracker."
												+ "Parameters: [task] [date] [time]\n\t"
												+ "Example: " + COMMAND_WORD
												+ " 2 tk/wash dishes t/0900 d/081016";
	
	public static final String MESSAGE_SUCCESS = "Task edited: %1$s";
	public static final String MESSAGE_NO_SUCH_TASK = "This task does not exist in the Task-Tracker";
	
	private Task toEdit;
	private final int editNum;
	private String message;
	private Date date1;
	private Date date2;
	private boolean isFloating = true;
	private boolean isEvent = false;
	private boolean hasDeadline = false;
	
	public EditCommand(int targetVisibleIndex, String message) throws IllegalValueException{
		super(targetVisibleIndex);
		editNum = targetVisibleIndex;
		this.message = message;
	}
	
	public EditCommand(int targetVisibleIndex, String message, Date deadline) throws IllegalValueException{
	    this(targetVisibleIndex,message);    
	    this.date1 = deadline;
	    isFloating = false;
	    hasDeadline = true;
	}
	
    public EditCommand(int targetVisibleIndex, String message, Date start, Date end) throws IllegalValueException{
        this(targetVisibleIndex,message);    
        this.date1 = start;
        this.date1 = end;
        isFloating = false;
        isEvent = true;
    }	
	
	@Override
	public CommandResult execute() {
		try {
		    if (hasDeadline) {
		        toEdit = new Task(message, date1);
		    }
		    else if (isEvent) {
		        toEdit = new Task(message, date1, date2);
		    }
		    else {
		        toEdit = new Task(message);
		    }
			DeleteCommand deleted = new DeleteCommand(editNum);
			deleted.execute();

			AddCommand added = new AddCommand(toEdit);
			added.execute();
			return new CommandResult(String.format(MESSAGE_SUCCESS, toEdit));
		}catch (IndexOutOfBoundsException ie) {
			return new CommandResult("The task index provided is invalid");
		}catch (TaskNotFoundException e) {
			return new CommandResult("Task does not exist in task-tracker");
		}catch (IllegalValueException e) {
			return new CommandResult(e.getMessage());
		}
	}
	
	
}
