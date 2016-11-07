//@@author A0139750B
package main.testutil;

import main.commons.exceptions.IllegalValueException;
import main.model.TaskTracker;
import main.model.task.PriorityType;
import main.model.task.Task;
import main.model.task.UniqueTaskList;

/**
 * Populates the testing app with 9 sample tasks which consists of 3 floating, 3
 * deadline and 3 event tasks
 */
public class TypicalTestTasks {

	public static TestTask floating1, floating2, floating3, deadline1, deadline2, deadline3, event1, event2, event3;

	public TypicalTestTasks() {
		try {
			floating1 = new TaskBuilder().withMessage("clean room").withPriority(PriorityType.LOW).build();
			floating2 = new TaskBuilder().withMessage("start studying").withPriority(PriorityType.HIGH).build();
			floating3 = new TaskBuilder().withMessage("fly kite").withPriority(PriorityType.NORMAL).build();

			deadline1 = new TaskBuilder().withMessage("return books").withDate(TestUtil.setDate(2016, 11, 10, 12, 00))
					.withPriority(PriorityType.HIGH).build();
			deadline2 = new TaskBuilder().withMessage("buy groceries").withDate(TestUtil.setDate(2016, 10, 31, 9, 30))
					.withPriority(PriorityType.NORMAL).build();
			deadline3 = new TaskBuilder().withMessage("finish coding").withDate(TestUtil.setDate(2016, 11, 7, 11, 00))
					.withPriority(PriorityType.NORMAL).build();

			event1 = new TaskBuilder().withMessage("meetup with friends")
					.withDates(TestUtil.setDate(2016, 11, 20, 11, 00), TestUtil.setDate(2016, 11, 20, 21, 00))
					.withPriority(PriorityType.NORMAL).build();
			event2 = new TaskBuilder().withMessage("interview")
					.withDates(TestUtil.setDate(2016, 12, 2, 15, 00), TestUtil.setDate(2016, 12, 2, 15, 30))
					.withPriority(PriorityType.HIGH).build();
			event3 = new TaskBuilder().withMessage("competition")
					.withDates(TestUtil.setDate(2016, 12, 15, 8, 00), TestUtil.setDate(2016, 12, 21, 18, 00))
					.withPriority(PriorityType.HIGH).build();

		} catch (IllegalValueException e) {
			e.printStackTrace();
			assert false : "not possible";
		}
	}

	public static void loadTaskTrackerWithSampleData(TaskTracker ab) {
		try {
			ab.addTask(new Task(deadline2));
			ab.addTask(new Task(deadline1));

			ab.addTask(new Task(event1));
			ab.addTask(new Task(event2));

			ab.addTask(new Task(floating1));
			ab.addTask(new Task(floating2));

		} catch (UniqueTaskList.DuplicateTaskException e) {
			assert false : "not possible";
		}
	}

	// @@author A0139422J
	public TestTask[] getTypicalTasks() {
		return new TestTask[] { deadline2, deadline1, event1, event2, floating1, floating2 };
	}

	public TestTask[] getHighTasks() {
		return new TestTask[] { deadline1, event2, floating2 };
	}

	public TestTask[] getEventTasks() {
		return new TestTask[] { event1, event2 };
	}

	public TestTask[] getFloatingTasks() {
		return new TestTask[] { floating1, floating2 };
	}

	public TestTask[] getDeadlineTasks() {
		return new TestTask[] { deadline2, deadline1 };
	}

	public TestTask[] getLowTasks() {
		return new TestTask[] { floating1 };
	}

	public TestTask[] getNormalTasks() {
		return new TestTask[] { deadline2, event1 };
	}

	public TaskTracker getTypicalTaskTracker() {
		TaskTracker ab = new TaskTracker();
		loadTaskTrackerWithSampleData(ab);
		return ab;
	}
}
