//@@author A0139422J
package guitests;

import org.junit.Test;

import main.testutil.TypicalTestTasks;

import static org.junit.Assert.assertEquals;

public class CommandBoxTest extends TaskTrackerGuiTest {

    @Test
    public void commandBox_commandSucceeds_textCleared() {
        commandBox.runCommand(TypicalTestTasks.floating2.getAddCommand());
        assertEquals(commandBox.getCommandInput(), "");
    }

}
