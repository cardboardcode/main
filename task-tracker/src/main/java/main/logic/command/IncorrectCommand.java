package main.logic.command;

/**
 * Represents an incorrect command. Upon execution, produces some feedback to the user.
 * @author bey
 */
//@@author A0139422J
public class IncorrectCommand extends Command {

    public final String feedbackToUser;

    public IncorrectCommand (String feedbackToUser){
        this.feedbackToUser = feedbackToUser;
    }

    @Override
    public CommandResult execute() {
        indicateAttemptToExecuteIncorrectCommand();
        return new CommandResult(feedbackToUser);
    }

}
