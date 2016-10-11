package main.storage;

import main.commons.core.LogsCenter;
import main.commons.exceptions.DataConversionException;
import main.commons.util.FileUtil;
import main.model.ReadOnlyTaskTracker;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * A class to access AddressBook data stored as an xml file on the hard disk.
 */
public class XmlTaskTrackerStorage implements TaskTrackerStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlTaskTrackerStorage.class);

    private String filePath;

    public XmlTaskTrackerStorage(String filePath){
        this.filePath = filePath;
    }

    public String getTaskTrackerFilePath(){
        return filePath;
    }

    /**
     * Similar to {@link #readAddressBook()}
     * @param filePath location of the data. Cannot be null
     * @throws DataConversionException if the file is not in the correct format.
     */
    public Optional<ReadOnlyTaskTracker> readTaskTracker(String filePath) throws DataConversionException, FileNotFoundException {
        assert filePath != null;

        File taskTrackerFile = new File(filePath);

        if (!taskTrackerFile.exists()) {
            logger.info("TaskTracker file "  + taskTrackerFile + " not found");
            return Optional.empty();
        }

        ReadOnlyTaskTracker taskTrackerOptional = XmlFileStorage.loadDataFromSaveFile(new File(filePath));

        return Optional.of(taskTrackerOptional);
    }

    /**
     * Similar to {@link #saveAddressBook(ReadOnlyAddressBook)}
     * @param filePath location of the data. Cannot be null
     */
    public void saveTaskTracker(ReadOnlyTaskTracker taskTracker, String filePath) throws IOException {
        assert taskTracker != null;
        assert filePath != null;

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableTaskTracker(taskTracker));
    }

    @Override
    public Optional<ReadOnlyTaskTracker> readTaskTracker() throws DataConversionException, IOException {
        return readTaskTracker(filePath);
    }

    @Override
    public void saveTaskTracker(ReadOnlyTaskTracker taskTracker) throws IOException {
        saveTaskTracker(taskTracker, filePath);
    }
}