package main.logic.command;

import java.util.ArrayList;
import main.data.Task;

public class AddCommand extends Command {
    
    public static final String COMMAND_WORD = "add";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a task to the to do list. "
            + "Parameters: TASK \n"
            + "Example: " + COMMAND_WORD + " CS2103 Meeting";

    public static final String MESSAGE_SUCCESS = "New task added: %1$s";
//    public static final String MESSAGE_DUPLICATE_TASK = "This task is already in the to do list";
    
    Task toAdd;

    public AddCommand(Task task) {
        toAdd = task;
    }
    
    @Override
    public String execute(ArrayList<Task> list) {
        list.add(toAdd);
        return String.format(MESSAGE_SUCCESS,toAdd) ;
    }
   

}
