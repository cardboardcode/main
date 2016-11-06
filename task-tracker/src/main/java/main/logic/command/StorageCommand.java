//@@author A0142686X
package main.logic.command;

import java.io.IOException;

import main.commons.core.Config;
import main.commons.core.EventsCenter;
import main.commons.events.storage.FilePathChangedEvent;
import main.commons.exceptions.DataConversionException;
import main.commons.util.ConfigUtil;
import main.commons.util.FileUtil;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.storage.StorageManager;

public class StorageCommand extends Command {
    public static final String COMMAND_WORD = "storage";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes TaskTracker storage location to the specified path. The path must be of an xml file.\n" + "eg. "
            + "storage main/docs/NewXmlDoc.xml";

    public static final String MESSAGE_SUCCESS = "Successfully changed storage path to %1$s";
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
        }
        else if (!FileUtil.isValidPath(newStoragePath)) {
            return new CommandResult(MESSAGE_INVALID_PATH);
        }
        else {
            EventsCenter.getInstance().post(new FilePathChangedEvent(newStoragePath, model.getTaskTracker()));            
            return new CommandResult(String.format(MESSAGE_SUCCESS, newStoragePath));
        }                               
    }
}
