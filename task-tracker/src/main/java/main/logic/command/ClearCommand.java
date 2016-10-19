package main.logic.command;

import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.model.task.UniqueTaskList;
import main.ui.ListStatistics;

public class ClearCommand extends Command {
    
    public static final String COMMAND_WORD = "clear";
    public static final String MESSAGE_USAGE = COMMAND_WORD;
    public static final String MESSAGE_SUCCESS = "Task Tracker cleared";
    
    public ClearCommand() {}

    @Override
    public CommandResult execute() {
        model.resetData((ReadOnlyTaskTracker) new TaskTracker(new UniqueTaskList()));
        ListStatistics.updateStatistics();
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
