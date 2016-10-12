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
	private Task newTask;
	private boolean isFloating = true;
	private boolean isEvent = false;
	private boolean hasDeadline = false;
	
	public EditCommand(int targetVisibleIndex, Task newTask) throws IllegalValueException{
		super(targetVisibleIndex);
		editNum = targetVisibleIndex;
		this.newTask = newTask;
	}	
	
	@Override
	public CommandResult execute() {
		try {
		    if (hasDeadline) {
		        toEdit = newTask;
		    }
		    else if (isEvent) {
		        toEdit = newTask;
		    }
		    else {
		        toEdit = newTask;
		    }
		    try{
		    toEdit = model.getTaskfromIndex(editNum);
		    }
		    catch (TaskNotFoundException e){
		        return new CommandResult("Task does not exist in task-tracker");
		    }
			toEdit.replaceTask(newTask);

			
			return new CommandResult(String.format(MESSAGE_SUCCESS, toEdit));
		}catch (IndexOutOfBoundsException ie) {
			return new CommandResult("The task index provided is invalid");
		}
	}
	
	
}
