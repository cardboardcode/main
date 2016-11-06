//@@author A0139422J
package main.logic.command;

import main.commons.core.EventsCenter;
import main.commons.events.ui.ExitAppRequestEvent;
import main.logic.command.CommandResult;

/**
 * Terminates the program.
 */
public class ExitCommand extends Command {

	public static final String COMMAND_WORD = "exit";

	public static final String MESSAGE_EXIT_ACKNOWLEDGEMENT = "Exiting Tracker-Tracker as requested ...";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": exits Task-Tracker";

	public ExitCommand() {
		super();
	}

	@Override
	public CommandResult execute() {
		EventsCenter.getInstance().post(new ExitAppRequestEvent());
		return new CommandResult(MESSAGE_EXIT_ACKNOWLEDGEMENT);
	}

}
