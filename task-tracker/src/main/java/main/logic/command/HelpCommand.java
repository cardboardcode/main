//@@author A0139422J
package main.logic.command;

import main.commons.core.EventsCenter;
import main.commons.events.ui.ShowHelpRequestEvent;
import main.logic.command.CommandResult;

/**
 * Displays a new window that shows all available command in T-T.
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
