//@@author A0142686X    
package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.commons.core.Messages;
import main.logic.command.DoneCommand;
import main.model.task.ReadOnlyTask;
import main.testutil.TestTask;

public class DoneCommandTest extends TaskTrackerGuiTest {
    
    TestTask[] currentList = td.getTypicalTasks();
    int targetIndex = 1;
    
    @Test
    public void doneTest() {
        commandBox.runCommand("done " + (currentList.length + 1));
        assertResultMessage("Task does not exist in task-tracker");
        
        ReadOnlyTask doneTask = taskListPanel.getTask(targetIndex - 1);
        commandBox.runCommand("done " + targetIndex );   
        assertTrue(doneTask.getIsDone());
    }
}
