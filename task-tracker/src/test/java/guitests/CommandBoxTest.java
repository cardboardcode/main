package guitests;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CommandBoxTest extends TaskTrackerGuiTest {

    @Test
    public void commandBox_commandSucceeds_textCleared() {
        commandBox.runCommand(td.floating2.getAddCommand());
        assertEquals(commandBox.getCommandInput(), "");
    }

}
