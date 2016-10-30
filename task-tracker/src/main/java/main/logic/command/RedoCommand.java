//@@author A0142686X
package main.logic.command;

import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.model.UndoHistory;
import main.model.task.Task;
import main.model.task.UniqueTaskList;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;
/**
     * Redoes the previous undo'd command. 
     * Pops from redo stack and executes appropriate add/del/edit/clr commands
     * 
 */

public class RedoCommand extends Command {

    public static final String COMMAND_WORD = "redo";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Reverts the last known command undo.\n" + "eg. "
            + COMMAND_WORD;

    public static final String MESSAGE_SUCCESS = "Reverted last undo. ";
    public static final String MESSAGE_EMPTY_HISTORY = "There are no more undos before this.";

    public static final int ADD = 1;
    public static final int DEL = 2;
    public static final int EDIT = 3;
    public static final int DONE = 4;
    public static final int CLR = 5;
    
    private UndoHistory redoHistory;
    
    @Override
    public CommandResult execute() {
        if(ModelManager.redoStack.size()==0) {
            return new CommandResult(MESSAGE_EMPTY_HISTORY);
        }
        redoHistory=ModelManager.redoStack.pop();
        int ID=redoHistory.getID();
        
        if(ID==ADD) {
           redoAdd(redoHistory.getTasks().get(0));
           return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==DEL) {
            redoDelete(redoHistory.getTasks().get(0));
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==EDIT) {
            try {
                try {
                    redoEdit(redoHistory.getTasks().get(0), redoHistory.getTasks().get(1));
                } catch (DuplicateTaskException | TaskNotFoundException e) {
                    e.printStackTrace();
                }
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();
            }
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==CLR) {
            redoClear();
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==DONE) {
            try {
                redoDone(redoHistory.getTasks().get(0));
            } catch (IndexOutOfBoundsException | TaskNotFoundException e) {
                e.printStackTrace();
            }
            return new CommandResult(MESSAGE_SUCCESS);
        }
        return new CommandResult(MESSAGE_EMPTY_HISTORY);
    }
    
    private void redoAdd(Task task) {
        try {
            model.addTask(task);
        } catch (DuplicateTaskException e) {
            e.printStackTrace();
        }
    }
    private void redoDelete(Task task) {
        try {
            model.deleteTask(model.getIndexFromTask(task));
        } catch (TaskNotFoundException e) {
            e.printStackTrace();
        }
    }
    private void redoEdit(Task newTask, Task originalTask) throws DuplicateTaskException, IndexOutOfBoundsException, TaskNotFoundException {
        model.editTask(model.getIndexFromTask(originalTask), newTask);
    }
    
    private void redoClear() {
        model.resetData((ReadOnlyTaskTracker) new TaskTracker(new UniqueTaskList()));
    }
    
    private void redoDone(Task task) throws IndexOutOfBoundsException, TaskNotFoundException {
        model.doneTask(model.getIndexFromTask(task));
    }
//        
//    public void clearRedo() {
//        model.emptyRedoStack();
//    }
}
