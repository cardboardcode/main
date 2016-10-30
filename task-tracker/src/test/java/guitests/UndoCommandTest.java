//@@author A0142686X
package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.logic.command.UndoCommand;
import main.testutil.TestTask;

public class UndoCommandTest extends TaskTrackerGuiTest {   
    
    @Test
    public void undoTest() {
        TestTask[] currentList = td.getTypicalTasks();
        int taskIndex=1;
        
        
//        //when no previous undo
//        commandBox.runCommand("undo");
//        assertResultMessage(UndoCommand.MESSAGE_EMPTY_HISTORY);
        
        //test to undo add
        commandBox.runCommand(td.deadline3.getAddCommand());
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS); 
        
        //test to undo delete
        commandBox.runCommand("delete " + taskIndex);
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS); 
        
        //test to undo clear
        commandBox.runCommand("clear");
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS); 
        
        //test to undo done
        commandBox.runCommand("done " + taskIndex);
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS); 
      
        //test multiple undo
        commandBox.runCommand(td.deadline2.getAddCommand());
        commandBox.runCommand(td.event1.getAddCommand());
        commandBox.runCommand("undo");
        commandBox.runCommand("undo");
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS); 
        
        //test to undo edit
        commandBox.runCommand("edit " + taskIndex + " buy clothes");
        commandBox.runCommand("undo");
        assertResultMessage(UndoCommand.MESSAGE_SUCCESS);       
               
    }
    
}
