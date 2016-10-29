//@@author A0142686X
package main.logic.command;

import java.io.IOException;

import com.sun.glass.ui.monocle.linux.Input;

import main.commons.core.Config;
import main.commons.exceptions.DataConversionException;
import main.commons.util.ConfigUtil;
import main.model.ReadOnlyTaskTracker;
import main.model.TaskTracker;
import main.storage.StorageManager;

public class StorageCommand extends Command {
    public static final String COMMAND_WORD = "storage";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Changes TaskTracker storage location to the specified path. The path must be of an xml file.\n" + "eg. "
            + "storage main/docs/NewXmlDoc.xml";

    public static final String MESSAGE_SUCCESS = "Successfully changed storage path to %1$s. ";
    public static final String MESSAGE_DUPLICATE_PATH = "Storage path is already set to specified location!";
    public static final String MESSAGE_CONVERT_FAILIURE = "Could not read from config file.";
    public static final String MESSAGE_SAVE_FAILIURE = "Could not save tasks to the specified location.";
    public static final String MESSAGE_NO_XML = "XML file not found at the specified location.";
    
    public String newStoragePath;
    
    public StorageCommand(String newStoragePath) {
        this.newStoragePath = newStoragePath;
    }
    
    @Override
    public CommandResult execute() {
        if((newStoragePath.substring((newStoragePath.length() - 4))).equals(".xml") == false) {
            return new CommandResult(MESSAGE_NO_XML); 
        }
        
        try {
            String defaultConfigPath = Config.DEFAULT_CONFIG_FILE; 
            Config presentConfig = ConfigUtil.readConfig(defaultConfigPath).orElse(new Config());
            String previousPath = presentConfig.getTaskTrackerFilePath();
            
            presentConfig.setTaskTrackerFilePath(newStoragePath);
            ConfigUtil.saveConfig(presentConfig, defaultConfigPath);
            
            StorageManager currentStorage = new StorageManager(previousPath, presentConfig.getUserPrefsFilePath());
            StorageManager newStorage = new StorageManager(newStoragePath, presentConfig.getUserPrefsFilePath());
            
            ReadOnlyTaskTracker currentTaskTracker = currentStorage.readTaskTracker().orElse(new TaskTracker());
            newStorage.saveTaskTracker(currentTaskTracker);
            
   
            return new CommandResult(MESSAGE_SUCCESS);            
        } catch (DataConversionException e) {
            e.printStackTrace();
            return new CommandResult(MESSAGE_CONVERT_FAILIURE);
        } catch (IOException e) {
            e.printStackTrace();
            return new CommandResult(MESSAGE_SAVE_FAILIURE);                        
        }        
    }
}
