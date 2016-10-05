package main.logic.command;

import java.util.ArrayList;

import main.data.Task;

public class HelpCommand extends Command {
    
    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = COMMAND_WORD;

    public HelpCommand() {}
    
    @Override
    public String execute(ArrayList<Task> list) {
        return getCommandsList();
    }
    
    private String getCommandsList() {
        String str = "";
        str += AddCommand.MESSAGE_USAGE + "\n";
        str += ListCommand.MESSAGE_USAGE + "\n";
        str += HelpCommand.MESSAGE_USAGE;
        
        return str;
    }
}
