//@@author A0142686X
package main.storage;

import main.commons.exceptions.DataConversionException;
import main.model.ReadOnlyTaskTracker;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents storage for TaskTracker.
 */
public interface TaskTrackerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getTaskTrackerFilePath();

    /**
     * Returns TaskTracker data as a ReadOnlyTaskTracker.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskTracker> readTaskTracker() throws DataConversionException, IOException;

    Optional<ReadOnlyTaskTracker> readTaskTracker(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given ReadOnlyTaskTracker to the storage.
     * @param taskTracker cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTaskTracker(ReadOnlyTaskTracker taskTracker) throws IOException;
    
    void saveTaskTracker(ReadOnlyTaskTracker taskTracker, String filePath) throws IOException;
    
    void setTaskTrackerFilePath(String filepath);

}
