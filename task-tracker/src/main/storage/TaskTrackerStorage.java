package main.storage;

import main.commons.exceptions.DataConversionException;
import main.model.ReadOnlyAddressBook;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents a storage for {@link seedu.address.model.AddressBook}.
 */
public interface TaskTrackerStorage {

    /**
     * Returns the file path of the data file.
     */
    String getTaskTrackerFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyTaskTracker}.
     *   Returns {@code Optional.empty()} if storage file is not found.
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyTaskTracker> readTaskTracker() throws DataConversionException, IOException;

    /**
     * @see #getAddressBookFilePath()
     */
    Optional<ReadOnlyTaskTracker> readTaskTracker(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyAddressBook} to the storage.
     * @param addressBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveTaskTracker(ReadOnlyTaskTracker taskTracker) throws IOException;

    /**
     * @see #saveAddressBook(ReadOnlyAddressBook)
     */
    void saveTaskTracker(ReadOnlyTaskTracker taskTracker, String filePath) throws IOException;

}
