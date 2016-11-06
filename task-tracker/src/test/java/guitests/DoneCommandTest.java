//@@author A0142686X    
package guitests;

import static org.junit.Assert.*;
import org.junit.Test;

import main.model.task.ReadOnlyTask;
import main.testutil.TestTask;

/**
 * Evaluates the done command when it marks an invalid and valid task as done
 */
public class DoneCommandTest extends TaskTrackerGuiTest {
    
    TestTask[] currentList = td.getTypicalTasks();
    int targetIndex = 1;
    
    @Test
    public void doneTest() {
        //To test if enetered index is greater than list size
        commandBox.runCommand("done " + (currentList.length + 1));
        assertResultMessage("Task does not exist in task-tracker");
        
        //To test if a task has been marked done
        ReadOnlyTask doneTask = taskListPanel.getTask(targetIndex - 1);
        commandBox.runCommand("done " + targetIndex );   
        assertTrue(doneTask.getIsDone());
    }
}
