//@@author A0139422J
package guitests;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
import guitests.guihandles.CommandBoxHandle;

/**
 * Evaluates the hot keys function: Esc to minimize, Up and Down arrow keys to
 * toggle command history and F1 to change color theme Warning: Color theme test
 * has to be done manually
 */
public class HotKeysTest extends TaskTrackerGuiTest {

	@Test
	public void pressHotKeys() {
		// minimizes window when Esc key is pressed
		assertWindowIsMinimized(commandBox.minimizeWindow());

		// scrolling up command history
		commandBox.runCommand("list");
		assertCommandToggledUp(commandBox.scrollUpCommandHistory());

		// scrolling down command history
		commandBox.runCommand("list");
		commandBox.runCommand("find");
		commandBox.runCommand("add");
		assertCommandToggledDown(commandBox.navigateCommandHistory());

		// change color themes of T-T
		commandBox.changeColorTheme();
	}

	private void assertWindowIsMinimized(CommandBoxHandle commandBox) {
		assertTrue(stage.isIconified());
	}

	private void assertCommandToggledUp(CommandBoxHandle commandBox) {
		assertEquals(commandBox.getCommandInput(), "list");
	}

	private void assertCommandToggledDown(CommandBoxHandle scrollUpCommandHistory) {
		assertEquals(commandBox.getCommandInput(), "find");
	}

}
