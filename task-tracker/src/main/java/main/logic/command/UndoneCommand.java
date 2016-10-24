package main.logic.command;

import main.model.task.UniqueTaskList.TaskNotFoundException;

public class UndoneCommand extends Command{
    
    public static final String COMMAND_WORD = "undone";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Removes the done indication of an existing task\n"
            + "Parameters: <task index>\n"
            + "Eg: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SUCCESS = "The following task is undone: %1$s";
    public static final String MESSAGE_TASK_ALREADY_DONE = "This task is already not marked as done";
    
    private int taskIndex;

    public UndoneCommand(int targetVisibleIndex) {
        super(targetVisibleIndex);
        
        this.taskIndex = targetVisibleIndex;
    }


    @Override
    public CommandResult execute() {
        try {
            //Remove comment after Model component adds a setIsDone() method which changes the value of 
            //it isDone attribute.
            model.getTaskfromIndex(taskIndex).setIsUnDone();
            return new CommandResult(String.format(MESSAGE_SUCCESS, taskIndex));

        } catch (IndexOutOfBoundsException ie) {
            return new CommandResult("The task index provided is invalid");
        } 
        catch (TaskNotFoundException pnfe) {
            return new CommandResult("Task does not exist in task-tracker");
        }
    }
    
}
