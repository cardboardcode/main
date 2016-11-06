//@@author A0142686X
package main.logic.command;

import main.commons.core.EventsCenter;
import main.commons.events.storage.FilePathChangedEvent;
import main.commons.util.FileUtil;

/**
 * Changes the path of the file storage according the directory provided by the user.
 */
public class StorageCommand extends Command {
	public static final String COMMAND_WORD = "storage";

	public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes TaskTracker storage location to the specified path. The path must be of an xml file.\n"
															+ "eg. " + "storage main/docs/NewXmlDoc.xml";

	public static final String MESSAGE_SUCCESS = "Successfully changed storage path";
	public static final String MESSAGE_DUPLICATE_PATH = "Storage path is already set to specified location!";
	public static final String MESSAGE_CONVERT_FAILIURE = "Could not read from config file.";
	public static final String MESSAGE_SAVE_FAILIURE = "Could not save tasks to the specified location.";
	public static final String MESSAGE_NO_XML = "XML file not found at the specified location.";
	public static final String MESSAGE_INVALID_PATH = "File path given cannot be resolved";

	public String newStoragePath;

	public StorageCommand(String newStoragePath) {
		this.newStoragePath = newStoragePath;
	}

	@Override
	public CommandResult execute() {

		if ((newStoragePath.substring((newStoragePath.length() - 4))).equals(".xml") == false) {
			return new CommandResult(MESSAGE_NO_XML);
		} else if (!FileUtil.isValidPath(newStoragePath)) {
			return new CommandResult(MESSAGE_INVALID_PATH);
		} else {
			EventsCenter.getInstance().post(new FilePathChangedEvent(newStoragePath, model.getTaskTracker()));
			return new CommandResult(MESSAGE_SUCCESS);
		}
	}
}
