//@@author A0142686X
package guitests;

import static org.junit.Assert.*;
import org.junit.Test;
import main.logic.command.UndoCommand;
import main.testutil.TestTask;
import main.testutil.TypicalTestTasks;

/**
 * Evaluates the undo command when it undoes an add, delete, clear, done,
 * multiple undo and edit commands
 */
public class UndoCommandTest extends TaskTrackerGuiTest {   
    
    @Test
    public void undoTest() {
        TestTask[] currentList = td.getTypicalTasks();
        int taskIndex=2;
        
        //test to undo add
        commandBox.runCommand(TypicalTestTasks.deadline3.getAddCommand());
        assertUndoSuccess(currentList); 
        
        //test to undo delete
        commandBox.runCommand("delete " + taskIndex);
        assertUndoSuccess(currentList); 
        
        //test to undo clear
        commandBox.runCommand("clear");
        assertUndoSuccess(currentList); 
        
        //test to undo done
        commandBox.runCommand("done " + taskIndex);
        assertUndoSuccess(currentList); 
      
        //test multiple undo
        commandBox.runCommand(TypicalTestTasks.deadline2.getAddCommand());
        commandBox.runCommand(TypicalTestTasks.event1.getAddCommand());
        commandBox.runCommand("undo");
        assertUndoSuccess(currentList); 
        
        //test to undo edit
        commandBox.runCommand("edit " + taskIndex + " buy clothes");
        assertUndoSuccess(currentList);                      
    }

    //Checks if listpanel matches the task list
    private void assertUndoSuccess(TestTask[] currentList) {
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);
    }    
}
