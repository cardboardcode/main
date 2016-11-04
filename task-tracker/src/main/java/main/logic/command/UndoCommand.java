//@@author A0142686X
package main.logic.command;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;

import main.model.ModelManager;
import main.model.UndoHistory;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;
    
/**
     *Undoes the previous command. 
     *Maintains a stack of changes made by last entered command
     *Pushes the undo'd command to redo stack
     *
 */ 
public class UndoCommand extends Command {

    public static final String COMMAND_WORD = "undo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the last known command input.\n" + "eg. "
            + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Reverted last command. ";
    public static final String MESSAGE_EMPTY_HISTORY = "There are no more inputs before this.";

    public static final int ADD = 1;
    public static final int DEL = 2;
    public static final int EDIT = 3;
    public static final int DONE = 4;
    public static final int CLR = 5;
    
    private UndoHistory undoHistory;

    @Override
    public CommandResult execute() {
        if(ModelManager.undoStack.size()==0) {
            return new CommandResult(MESSAGE_EMPTY_HISTORY);
        }
        undoHistory=ModelManager.undoStack.pop();
        ModelManager.redoStack.push(undoHistory);
        int ID=undoHistory.getID();
        
        if(ID==ADD) {
           undoAdd(undoHistory.getTasks().get(0));
           return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==DEL) {
            undoDelete(undoHistory.getTasks().get(0));
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==CLR) {
            undoClear(undoHistory.getTasks());
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==EDIT) {
            try {
                undoEdit(undoHistory.getTasks().get(0), undoHistory.getTasks().get(1));
            } catch (DuplicateTaskException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            } catch (TaskNotFoundException e) {
                e.printStackTrace();
            }
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==DONE) {
            try {
                undoDone(undoHistory.getTasks().get(0));
            } catch (DuplicateTaskException | TaskNotFoundException e) {
                e.printStackTrace();
            }
            return new CommandResult(MESSAGE_SUCCESS);
        }
        return new CommandResult(MESSAGE_EMPTY_HISTORY);
    }
    
    private void undoAdd(Task task) {
        try {
            model.deleteTaskUndoRedo(task);
        } catch (TaskNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void undoDelete(Task task) {
        try {
            model.addTaskUndoRedo(task);
        } catch (DuplicateTaskException e) {
            e.printStackTrace();
        }
    }
    private void undoEdit(Task newTask, Task originalTask) throws DuplicateTaskException, IndexOutOfBoundsException, TaskNotFoundException{
        model.editTaskUndoRedo(originalTask, newTask);
    }
    
    private void undoClear(ArrayList<Task> tasks) {
        model.clearTaskUndoRedo(tasks);
    }
    
    private void undoDone(Task task) throws DuplicateTaskException, TaskNotFoundException {
        model.doneTaskUndoRedo(task);
    }
//    
//    public void clearUndo() {
//        model.emptyUndoStack();
//    }
}
