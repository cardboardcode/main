//@@author A0139422J
package guitests;

import static org.junit.Assert.assertTrue;
import org.junit.Test;
import main.testutil.TestTask;

/**
 * Evaluates the list command when it lists events, deadlines, floating, low
 * priority, normal priority and high priority tasks
 */
public class ListCommandTest extends TaskTrackerGuiTest {

	@Test
	public void list() {
		TestTask[] filteredList = td.getHighTasks();

		// lists all events
		filteredList = td.getEventTasks();
		commandBox.runCommand("list events");
		assertTrue(taskListPanel.isListMatching(filteredList));
		assertResultMessage("Listed all pending events");

		// lists all deadlines
		filteredList = td.getDeadlineTasks();
		commandBox.runCommand("list deadlines");
		assertTrue(taskListPanel.isListMatching(filteredList));
		assertResultMessage("Listed all pending tasks with deadlines");

		// lists all floating
		filteredList = td.getFloatingTasks();
		commandBox.runCommand("list floating");
		assertTrue(taskListPanel.isListMatching(filteredList));
		assertResultMessage("Listed all pending floating tasks");

		// lists all low priority tasks
		filteredList = td.getLowTasks();
		commandBox.runCommand("list low");
		assertTrue(taskListPanel.isListMatching(filteredList));
		assertResultMessage("Listed all pending low priority tasks");

		// lists all normal priority tasks
		filteredList = td.getNormalTasks();
		commandBox.runCommand("list normal");
		assertTrue(taskListPanel.isListMatching(filteredList));
		assertResultMessage("Listed all pending normal priority tasks");

		// lists all high priority tasks
		filteredList = td.getHighTasks();
		commandBox.runCommand("list high");
		assertTrue(taskListPanel.isListMatching(filteredList));
		assertResultMessage("Listed all pending high priority tasks");

	}
}
