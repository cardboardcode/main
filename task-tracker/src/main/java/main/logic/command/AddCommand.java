package main.logic.command;

import main.model.task.Task;
import main.model.task.UniqueTaskList;
import main.ui.ListStatistics;

/**
 * Adds a task to the task-tracker storage.
 * @author bey
 */
public class AddCommand extends Command {
    
    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the to do list.\n"
            + "Parameters: [message] [MM/DD//YY] [time] \n"
            + "Eg: " + COMMAND_WORD + " CS2103 Week 8 Tutorial 10132016 0920";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
    public static final String MESSAGE_DUPLICATE_TASK = "This task is already in the to do list";
    
    Task toAdd;

    public AddCommand(Task task) {
        toAdd = task;
    }

    /**
     * //Remember to add validator in Model component
		//Possible to add Traynotification here.
     */
	@Override
	public CommandResult execute() {
		try {
            model.addTask(toAdd);
            ListStatistics.updateStatistics();
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
		}
		catch (UniqueTaskList.DuplicateTaskException e) {
		    return new CommandResult(MESSAGE_DUPLICATE_TASK);
		}
        
	}
}
