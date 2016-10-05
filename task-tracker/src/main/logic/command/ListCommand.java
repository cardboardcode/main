package main.logic.command;

import java.util.ArrayList;

import main.data.Task;

public class ListCommand extends Command {
    
    public static final String COMMAND_WORD = "list";
    public static final String MESSAGE_USAGE = COMMAND_WORD;
            
    public ListCommand() {}
    
    @Override
    public String execute(ArrayList<Task> list) {
        return format(list);
    }

    public String format(ArrayList<Task> list) {
        String str = "";
        for (Task task : list) {
            str += task + "\n";
        }
        return str;
    }
}
