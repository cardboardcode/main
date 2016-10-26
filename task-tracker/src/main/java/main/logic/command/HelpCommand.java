package main.logic.command;

//@@author A0139422J
import main.commons.core.EventsCenter;
import main.commons.events.ui.ShowHelpRequestEvent;
import main.logic.command.CommandResult;

/**
 * Format full help instructions for every command for display.
 * @author bey
 */

public class HelpCommand extends Command {
    
	public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    public HelpCommand() {}

    @Override
    public CommandResult execute() {
        EventsCenter.getInstance().post(new ShowHelpRequestEvent());
        return new CommandResult(SHOWING_HELP_MESSAGE);
    }
}
