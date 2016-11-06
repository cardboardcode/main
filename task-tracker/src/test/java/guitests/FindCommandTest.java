//@@author A0139422J
package guitests;

import org.junit.Test;
import main.logic.command.FindCommand;

/**
 * Evaluates the find command when it searches for any tasks regarding a certain
 * keyword "friends"
 */
public class FindCommandTest extends TaskTrackerGuiTest {

	@Test
	public void find() {
		// finds any tasks with the "friends" keyword
		commandBox.runCommand("find" + " friends");
		assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS, 6));
	}
}
