//@@author A0142686X
package main.storage;

import main.commons.util.XmlUtil;
import main.commons.exceptions.DataConversionException;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.FileNotFoundException;

/**
 * Stores tasktracker data in an XML file
 */
public class XmlFileStorage {
    /**
     * Saves the given tasktracker data to the specified file.
     */
    public static void saveDataToFile(File file, XmlSerializableTaskTracker taskTracker)
            throws FileNotFoundException {
        try {
            XmlUtil.saveDataToFile(file, taskTracker);
        } catch (JAXBException e) {
            assert false : "Unexpected exception " + e.getMessage();
        }
    }

    /**
     * Returns tasktracker in the file or an empty tasktracker
     */
    public static XmlSerializableTaskTracker loadDataFromSaveFile(File file) throws DataConversionException,
                                                                            FileNotFoundException {
        try {
            return XmlUtil.getDataFromFile(file, XmlSerializableTaskTracker.class);
        } catch (JAXBException e) {
            throw new DataConversionException(e);
        }
    }

}
