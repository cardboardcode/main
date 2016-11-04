//@@author A0142686X
package main.storage;

import com.google.common.eventbus.Subscribe;
import main.commons.core.ComponentManager;
import main.commons.core.Config;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.events.model.LoadTaskTrackerEvent;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.storage.DataSavingExceptionEvent;
import main.commons.events.storage.FilePathChangedEvent;
import main.commons.exceptions.DataConversionException;
import main.commons.util.ConfigUtil;
import main.commons.util.FileUtil;
import main.commons.util.StringUtil;
import main.model.ReadOnlyTaskTracker;
import main.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of TaskTracker data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskTrackerStorage taskTrackerStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskTrackerStorage taskTrackerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskTrackerStorage = taskTrackerStorage;
        this.userPrefsStorage = userPrefsStorage;
        EventsCenter.getInstance().registerHandler(this);
    }

    public StorageManager(String taskTrackerFilePath, String userPrefsFilePath) {
        this(new XmlTaskTrackerStorage(taskTrackerFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
        EventsCenter.getInstance().registerHandler(this);
    }

    // ================ UserPrefs methods ==============================

    @Override
    public Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException {
        return userPrefsStorage.readUserPrefs();
    }

    @Override
    public void saveUserPrefs(UserPrefs userPrefs) throws IOException {
        userPrefsStorage.saveUserPrefs(userPrefs);
    }


    // ================ TaskTracker methods ==============================

    @Override
    public String getTaskTrackerFilePath() {
        return taskTrackerStorage.getTaskTrackerFilePath();
    }

    @Override
    public Optional<ReadOnlyTaskTracker> readTaskTracker() throws DataConversionException, IOException {
        return readTaskTracker(taskTrackerStorage.getTaskTrackerFilePath());
    }

    @Override
    public Optional<ReadOnlyTaskTracker> readTaskTracker(String filePath) throws DataConversionException, IOException {
        logger.fine("Attempting to read data from file: " + filePath);
        return taskTrackerStorage.readTaskTracker(filePath);
    }

    @Override
    public void saveTaskTracker(ReadOnlyTaskTracker taskTracker) throws IOException {
        saveTaskTracker(taskTracker, taskTrackerStorage.getTaskTrackerFilePath());
    }

    @Override
    public void saveTaskTracker(ReadOnlyTaskTracker taskTracker, String filePath) throws IOException {
        logger.fine("Attempting to write to data file: " + filePath);
        taskTrackerStorage.saveTaskTracker(taskTracker, filePath);
    }


    @Override
    @Subscribe
    public void handleTaskTrackerChangedEvent(TaskTrackerChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Local data changed, saving to file"));
        try {
            saveTaskTracker(event.data);
        } catch (IOException e) {
            raise(new DataSavingExceptionEvent(e));
        }
    }

    //@@author A0144132W
    @Override
    public void setTaskTrackerFilePath(String filepath) {
        assert FileUtil.isValidPath(filepath);
        taskTrackerStorage.setTaskTrackerFilePath(filepath);
    }
    
    @Override
    @Subscribe
    public void handleFilePathChangedEvent(FilePathChangedEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "Attempting to set and save to new file path"));

        String filePath = event.getFilePath();
        ReadOnlyTaskTracker taskTracker = event.getTaskTracker();
        
        setTaskTrackerFilePath(filePath);

        ReadOnlyTaskTracker originalData = originalData(filePath);
        if (originalData != null) {
            taskTracker = originalData;
        }
        else {
            saveChanges(filePath, taskTracker);
        }      

        logger.info(taskTracker.getTaskList().size() + "");
        
        EventsCenter.getInstance().post(new LoadTaskTrackerEvent(taskTracker));
        
    }

    private void saveChanges(String filePath, ReadOnlyTaskTracker taskTracker) {
        try {
            saveTaskTracker(taskTracker);
            ConfigUtil.saveConfig(new Config(filePath), Config.DEFAULT_CONFIG_FILE);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
    }
    
    private ReadOnlyTaskTracker originalData(String filePath) {
        Optional<ReadOnlyTaskTracker> taskTrackerOptional;

        try {
            taskTrackerOptional = readTaskTracker();
            
            if (taskTrackerOptional.isPresent()) {
                logger.info("File already has data. Loading original data");
            }
            else {
                logger.info("Saving to new file path");
            }
            
            return taskTrackerOptional.orElse(null);
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Overwriting file with current data");
            return null;
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. Prompt for new location");
            return null;
        }
    }

}