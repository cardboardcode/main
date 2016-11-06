//@@author A0139422J
package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.Node;
import javafx.stage.Stage;
import main.model.task.ReadOnlyTask;

/**
 * Provides a handle to a task card in the task list panel.
 */
public class TaskCardHandle extends GuiHandle {
	private static final String MESSAGE_FIELD_ID = "#message";

	private Node node;

	public TaskCardHandle(GuiRobot guiRobot, Stage primaryStage, Node node) {
		super(guiRobot, primaryStage, null);
		this.node = node;
	}

	protected String getTextFromLabel(String fieldId) {
		return getTextFromLabel(fieldId, node);
	}

	public String getMessage() {
		return getTextFromLabel(MESSAGE_FIELD_ID);
	}

	public boolean isSameTask(ReadOnlyTask task) {
		return true;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof TaskCardHandle) {
			TaskCardHandle handle = (TaskCardHandle) obj;
			return getMessage().equals(handle.getMessage().equals(handle.getMessage()));
		}
		return super.equals(obj);
	}

	@Override
	public String toString() {
		return getMessage();
	}
}
