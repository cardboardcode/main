package main.logic.command;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.logging.Logger;

import main.model.ModelManager;
import main.model.UndoHistory;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.model.task.UniqueTaskList.TaskNotFoundException;
import java.util.logging.Logger;
import main.commons.core.LogsCenter;
import main.logic.parser.MainParser;
/**
 *Undoes the previous command. 
 *Maintains a stack of changes made by last entered command
 * 
 * 
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
    private static final Logger logger = LogsCenter.getLogger(MainParser.class);

    @Override
    public CommandResult execute() {
        if(ModelManager.undoStack.size()==0){
            return new CommandResult(MESSAGE_EMPTY_HISTORY);
        }
        undoHistory=ModelManager.undoStack.pop();
        ModelManager.redoStack.push(undoHistory);
        int ID=undoHistory.getID();
        
        if(ID==ADD){
           undoAdd(undoHistory.getTasks().get(0));
           return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==DEL){
            undoDelete(undoHistory.getTasks().get(0));
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==CLR){
            undoClear(undoHistory.getTasks());
            return new CommandResult(MESSAGE_SUCCESS);
        }
        if(ID==EDIT){
            try {
                undoEdit(undoHistory.getTasks().get(0), undoHistory.getTasks().get(1));
            } catch (DuplicateTaskException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (TaskNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            //logger.info(String.valueOf(ID));
            return new CommandResult(MESSAGE_SUCCESS);
        }
        
//        if(ID==DONE){
//            ndoDone(undoHistory.getTasks().get(1));
//            return new CommandResult(MESSAGE_SUCCESS);
//        }
        return new CommandResult(MESSAGE_EMPTY_HISTORY);
    }
    
    private void undoAdd(Task task){
        try {
            model.deleteTaskUndo(task);
        } catch (TaskNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void undoDelete(Task task){
        try {
            model.addTaskUndo(task);
        } catch (DuplicateTaskException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private void undoEdit(Task newTask, Task originalTask) throws DuplicateTaskException, IndexOutOfBoundsException, TaskNotFoundException{
        model.editTaskUndo(model.getIndexFromTask(originalTask), newTask);
    }
    
    private void undoClear(ArrayList<Task> tasks){
        model.clearTaskUndo(tasks);
    }
    
//    private void undoDone(ReadOnlyTask task){
//        
//    }

}
