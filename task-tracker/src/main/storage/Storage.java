package main.storage;

import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.storage.DataSavingExceptionEvent;
import main.commons.exceptions.DataConversionException;
import main.model.model.ReadOnlyTaskTracker;
import main.model.model.UserPrefs;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;

/**
 * API of the Storage component
 */
public interface Storage extends TaskTrackerStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;

    @Override
    String getTaskTrackerFilePath();

    @Override
    Optional<ReadOnlyTaskTracker> readTaskTracker() throws DataConversionException, IOException;

    @Override
    void saveTaskTracker(ReadOnlyTaskTracker taskTracker) throws IOException;

    /**
     * Saves the current version of the Address Book to the hard disk.
     *   Creates the data file if it is missing.
     * Raises {@link DataSavingExceptionEvent} if there was an error during saving.
     */
    void handleTaskTrackerChangedEvent(TaskTrackerChangedEvent abce);
}
