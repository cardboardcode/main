package main.logic.command;

import main.logic.command.CommandResult;

/**
 * Format full help instructions for every command for display.
 * @author bey
 */

public class HelpCommand extends Command {
    
    public static final String COMMAND_WORD = "help";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Displays list of available commands.";

    public HelpCommand() {}
    
    private String getCommandsList() {
        String str = "";
        str += AddCommand.MESSAGE_USAGE + "\n\n";
        str += EditCommand.MESSAGE_USAGE + "\n\n";
        str += ExitCommand.MESSAGE_USAGE + "\n\n";
//        str += ListCommand.MESSAGE_USAGE + "\n";
        str += HelpCommand.MESSAGE_USAGE + "\n\n";
        str += DeleteCommand.MESSAGE_USAGE + "\n\n";
      
        return str;
    }

	@Override
	public CommandResult execute() {
		return new CommandResult(getCommandsList());
	}
}
