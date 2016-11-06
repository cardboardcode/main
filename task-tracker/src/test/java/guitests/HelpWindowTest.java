package guitests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import guitests.guihandles.HelpWindowHandle;

/**
 * Evaluates the help command when it opens the help window.
 */
public class HelpWindowTest extends TaskTrackerGuiTest{
	
	@Test
    public void openHelpWindow() {
        assertHelpWindowOpen(commandBox.runHelpCommand());
    }

    private void assertHelpWindowOpen(HelpWindowHandle helpWindowHandle) {
        assertTrue(helpWindowHandle.isWindowOpen());
        helpWindowHandle.closeWindow();
    }
}
