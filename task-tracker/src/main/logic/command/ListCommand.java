package main.logic.command;

import java.util.ArrayList;

import main.data.Task;

/**
 * Lists all persons in the address book to the user.
 */

public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD;
            
    public ListCommand() {}
    
    public String format() {
        
    	ArrayList<Task> list = model.getGeneralList();
    	String str = "";
        for (Task task : list) {
            str += task.getMessage() + "\n";
        }
        return str;
    }

	@Override
	public CommandResult execute() {
		return new CommandResult(format());
	}
}
