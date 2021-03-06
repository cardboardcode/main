//@@author A0142686X
package main.storage;

import main.commons.exceptions.DataConversionException;
import main.model.UserPrefs;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for UserPrefs.
 */
public interface UserPrefsStorage {

    /**
     * Returns UserPrefs data from storage.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    /**
     * Saves the given UserPrefs to the storage.
     * @param userPrefs cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveUserPrefs(UserPrefs userPrefs) throws IOException;
    
}
