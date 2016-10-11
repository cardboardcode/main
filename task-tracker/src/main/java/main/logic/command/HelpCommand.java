package main.logic.command;

import main.logic.command.CommandResult;

/**
 * Format full help instructions for every command for display.
 * @author bey
 */

public class HelpCommand extends Command {
    
    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = COMMAND_WORD;

    public HelpCommand() {}
    
    private String getCommandsList() {
        String str = "";
        str += AddCommand.MESSAGE_USAGE + "\n";
        str += EditCommand.MESSAGE_USAGE + "\n";
        str += ExitCommand.MESSAGE_USAGE + "\n";
        str += ListCommand.MESSAGE_USAGE;
        str += HelpCommand.MESSAGE_USAGE;
        str += DeleteCommand.MESSAGE_USAGE;
      
        return str;
    }

	@Override
	public CommandResult execute() {
		return new CommandResult(getCommandsList());
	}
}
