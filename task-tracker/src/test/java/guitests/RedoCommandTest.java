//@@author A0142686X
package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import guitests.guihandles.TaskCardHandle;
import main.logic.command.RedoCommand;
import main.logic.command.UndoCommand;
import main.model.task.ReadOnlyTask;
import main.testutil.TestTask;
import main.testutil.TestUtil;

public class RedoCommandTest extends TaskTrackerGuiTest {    
    
    @Test
    public void redoTest() {
        
        TestTask[] currentList = td.getTypicalTasks();
        int taskIndex=2;
        
        
//        //redo when no previous undo
//        commandBox.runCommand("redo");
//        assertResultMessage(RedoCommand.MESSAGE_EMPTY_HISTORY);
//        
        //redo add
        currentList = TestUtil.addTasksToList(currentList, td.deadline3);        
        commandBox.runCommand(td.deadline3.getAddCommand());
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");        
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);

        
        //redo delete
        currentList = TestUtil.removeTaskFromList(currentList, taskIndex);
        commandBox.runCommand("delete " + taskIndex);      
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");     
        assertTrue(taskListPanel.isListMatching(currentList));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
                
        //redo edit
        currentList = TestUtil.replaceTaskFromList(currentList, td.event2, taskIndex);
        commandBox.runCommand("edit " + taskIndex + td.event2.getAddCommand().substring(3));
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");

        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
        
        //redo done        
        ReadOnlyTask doneTask = taskListPanel.getTask(taskIndex - 1);
        commandBox.runCommand("done " + taskIndex);   
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");
        assertTrue(doneTask.getIsDone());
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
        
        //redo clear
        commandBox.runCommand("clear");
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");
        assertListSize(0);
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
    }

}
