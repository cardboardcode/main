//@@author A0139422J
package main.logic.command;

import main.model.task.UniqueTaskList.TaskNotFoundException;

public class DoneCommand extends Command{
	
	public static final String COMMAND_WORD = "done";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Indicates a task to be done\n"
            + "Parameters: <task index>\n"
            + "Eg: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "The following task is done: %1$s";
    public static final String MESSAGE_TASK_ALREADY_DONE = "This task is already marked as done";
    
    private int taskIndex;

    public DoneCommand(int targetVisibleIndex) {
        super(targetVisibleIndex);
        
        this.taskIndex = targetVisibleIndex;
    }


    @Override
    public CommandResult execute() {
        try {
            model.doneTask(taskIndex);
            return new CommandResult(String.format(MESSAGE_SUCCESS, taskIndex+1));
        } 
        catch (IndexOutOfBoundsException ie) {
            return new CommandResult("The task index provided is invalid");
        } 
        catch (TaskNotFoundException pnfe) {
            return new CommandResult("Task does not exist in task-tracker");
        }
    }

}
