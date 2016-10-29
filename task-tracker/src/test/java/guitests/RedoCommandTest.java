//@@author A0142686X
package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Arrays;
import guitests.guihandles.TaskCardHandle;
import main.logic.command.RedoCommand;
import main.logic.command.UndoCommand;
import main.testutil.TestTask;
import main.testutil.TestUtil;

public class RedoCommandTest extends TaskTrackerGuiTest {
    TestTask[] currentList = td.getTypicalTasks();
    int taskIndex=1;
    
    @Test
    public void redoTest() {
        
        //redo when no previous undo
        commandBox.runCommand("redo");
        assertResultMessage(RedoCommand.MESSAGE_EMPTY_HISTORY);
        
        //redo add
        commandBox.runCommand(td.deadline3.getAddCommand());
        commandBox.runCommand("undo");
        commandBox.runCommand("redo");
        TestTask [] expectedList = TestUtil.addTasksToList(currentList, td.deadline3);
        assertTrue(taskListPanel.isListMatching(expectedList));
        assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
        
        
        expectedList = currentList;
        TestUtil.removeTasksFromList(currentList, td.deadline3);

        //redo delete
        TestUtil.removeTaskFromList(currentList, taskIndex);
        commandBox.runCommand("delete " + taskIndex);      
        //System.out.println("first "+Arrays.toString((taskListPanel.getListView()).toArray()));
        commandBox.runCommand("undo");
        //System.out.println("first "+taskListPanel.getNumberOfTasks());
        commandBox.runCommand("redo");     
        //System.out.println("second "+taskListPanel.getNumberOfTasks());        
        //System.out.println("second "+expectedList.length);
        //assertTrue(taskListPanel.isListMatching(expectedList));
        //assertResultMessage(RedoCommand.MESSAGE_SUCCESS);
                
        //TODO
    }

}
