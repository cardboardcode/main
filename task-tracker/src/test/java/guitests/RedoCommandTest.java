//@@author A0142686X
package guitests;

import static org.junit.Assert.*;
import org.junit.Test;

import main.logic.command.RedoCommand;
import main.model.task.ReadOnlyTask;
import main.testutil.TestTask;
import main.testutil.TestUtil;
import main.testutil.TypicalTestTasks;

/**
 * Tests Redo command for add, delete, done, edit and clear
 */
public class RedoCommandTest extends TaskTrackerGuiTest {    
    
    @Test
    public void redoTest() {
        
        TestTask[] currentList = td.getTypicalTasks();
        int taskIndex = 2;
  
        //test for redo add
        currentList = TestUtil.addTasksToList(currentList, TypicalTestTasks.deadline3);        
        commandBox.runCommand(TypicalTestTasks.deadline3.getAddCommand());
        runUndoRedo();        
        assertRedoSuccess(currentList);
        
        //test for redo delete
        currentList = TestUtil.removeTaskFromList(currentList, taskIndex);
        commandBox.runCommand("delete " + taskIndex);      
        runUndoRedo();     
        assertRedoSuccess(currentList);
                
        //test for redo edit
        currentList = TestUtil.replaceTaskFromList(currentList, TypicalTestTasks.deadline3, taskIndex - 1);
        commandBox.runCommand("edit " + taskIndex + TypicalTestTasks.deadline3.getAddCommand().substring(3));
        runUndoRedo();
        assertRedoSuccess(currentList);
        
        //test for redo done        
        ReadOnlyTask doneTask = taskListPanel.getTask(taskIndex - 1);
        commandBox.runCommand("done " + taskIndex);   
        runUndoRedo();
        assertTrue(doneTask.getIsDone());
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
        
        //test for redo clear
        commandBox.runCommand("clear");
        runUndoRedo();
        assertListSize(0);
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
    }

    //Checks if listpanel matches the modified list
    private void assertRedoSuccess(TestTask[] currentList) {
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
    }
    
    //runs undo followed by redo in the command box
    private void runUndoRedo() {
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");
    }
}
