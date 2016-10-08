package main.logic.command;

import static seedu.addressbook.ui.Gui.DISPLAYED_INDEX_OFFSET;

import java.util.ArrayList;
import java.util.List;

import main.commons.core.EventsCenter;
import main.commons.core.Messages;
import main.commons.events.ui.IncorrectCommandAttemptedEvent;
import main.data.Task;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 * @author bey
 */
public abstract class Command {
	protected Model model;
	private int targetIndex = -1;
//	/**
//     * @param targetIndex last visible listing index of the target task
//     */
	public Command(int targetIndex) {
        this.setTargetIndex(targetIndex);
    }
	
	public Command() {}
	
	public void setTargetIndex(int targetIndex) {
        this.targetIndex = targetIndex;
    }
    /**
     * Constructs a feedback message to summarise an operation that displayed a listing of persons.
     *
     * @param displaySize used to generate summary
     * @return summary message for persons displayed
     */
    public static String getMessageForPersonListShownSummary(int displaySize) {
        return String.format(Messages.MESSAGE_TASK_LISTED_OVERVIEW, displaySize);
    }

    /**
     * Executes the command and returns the result message.
     *
     * @return feedback message of the operation result for display
     */
    public abstract CommandResult execute();

    /**
     * Provides any needed dependencies to the command.
     * Commands making use of any of these should override this method to gain
     * access to the dependencies.
     */
    public void setData(Model model) {
        this.model = model;
    }

    /**
     * Raises an event to indicate an attempt to execute an incorrect command
     */
    protected void indicateAttemptToExecuteIncorrectCommand() {
        EventsCenter.getInstance().post(new IncorrectCommandAttemptedEvent(this));
    }
       
}
