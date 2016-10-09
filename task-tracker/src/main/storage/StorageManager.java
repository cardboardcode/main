package main.storage;

import com.google.common.eventbus.Subscribe;
import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.storage.DataSavingExceptionEvent;
import main.commons.exceptions.DataConversionException;
import main.model.model.ReadOnlyTaskTracker;
import main.model.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Manages storage of AddressBook data in local storage.
 */
public class StorageManager extends ComponentManager implements Storage {

    private static final Logger logger = LogsCenter.getLogger(StorageManager.class);
    private TaskTrackerStorage taskTrackerStorage;
    private UserPrefsStorage userPrefsStorage;


    public StorageManager(TaskTrackerStorage taskTrackerStorage, UserPrefsStorage userPrefsStorage) {
        super();
        this.taskTrackerStorage = taskTrackerStorage;
        this.userPrefsStorage = userPrefsStorage;
    }

    public StorageManager(String addressBookFilePath, String userPrefsFilePath) {
        this(new XmlTaskTrackerStorage(addressBookFilePath), new JsonUserPrefsStorage(userPrefsFilePath));
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


    // ================ AddressBook methods ==============================

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
    public void saveTaskTracker(ReadOnlyTaskTracker addressBook) throws IOException {
        saveTaskTracker(addressBook, taskTrackerStorage.getTaskTrackerFilePath());
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

}
