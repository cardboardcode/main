//@@author A0139422J
package guitests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import main.testutil.TestTask;
import main.testutil.TestUtil;

/**
 * Evaluates sort command when it sorts the list by name and then by date
 */
public class SortCommandTest extends TaskTrackerGuiTest {

	@Test
	public void sort() {
		TestTask[] currentList = td.getTypicalTasks();

		// Sorts the list by name
		commandBox.runCommand("sort" + " name");
		TestTask[] expectedList = TestUtil.sortTasksByName(currentList);
		assertTrue(taskListPanel.isListMatching(expectedList));

		// Sorts the list by date
		commandBox.runCommand("sort" + " date");
		expectedList = TestUtil.sortTasksByDate(currentList);
		assertTrue(taskListPanel.isListMatching(expectedList));
	}
}
