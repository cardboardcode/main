package main.logic.command;

import main.model.task.UniqueTaskList.TaskNotFoundException;

/**
 * Deletes a person identified using it's last displayed index from the address book.
 * Prints success message and the corresponding deleted index.
 * @author bey
 */

public class DeleteCommand extends Command {
	public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ":\n" 
            + "Deletes the person identified by the index number used in the last person listing.\n\t"
            + "Parameters: INDEX\n\t"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";
    
    private int taskIndex;

    public DeleteCommand(int targetVisibleIndex) {
        super(targetVisibleIndex);
        
        this.taskIndex = targetVisibleIndex;
    }


    @Override
    public CommandResult execute() {
        try {
            model.deleteTask(taskIndex);
            return new CommandResult(String.format(MESSAGE_DELETE_PERSON_SUCCESS, taskIndex));

        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult("The task index provided is invalid");
        } catch (TaskNotFoundException pnfe) {
            return new CommandResult("Task does not exist in task-tracker");
        }
    }
}
