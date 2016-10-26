package guitests;

import org.junit.Test;
import main.testutil.TypicalTestTasks;
import static org.junit.Assert.assertTrue;

//@@author A0139422J
public class ClearCommandTest extends TaskTrackerGuiTest {

    @Test
    public void clear() {

        //verify a non-empty list can be cleared
        assertTrue(taskListPanel.isListMatching(td.getTypicalTasks()));
        assertClearCommandSuccess();

        //verify other commands can work after a clear command
        commandBox.runCommand(TypicalTestTasks.event2.getAddCommand());
        assertTrue(taskListPanel.isListMatching(TypicalTestTasks.event2));
        commandBox.runCommand("delete 1");
        assertListSize(0);

        //verify clear command works when the list is empty
        assertClearCommandSuccess();
    }

    private void assertClearCommandSuccess() {
        commandBox.runCommand("clear");
        assertListSize(0);
        assertResultMessage("Task-Tracker has been cleared!");
    }
}
