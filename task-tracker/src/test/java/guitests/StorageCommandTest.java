package guitests;

import static org.junit.Assert.*;

import org.junit.Test;

import main.logic.command.StorageCommand;
import main.storage.Storage;

public class StorageCommandTest extends TaskTrackerGuiTest {
    
    String validPath = "src/test/data/sandbox/newfile.xml";
    String invalidPath = "src/test/data/sandbox/textfile.txt";
    String defaultPath = "src/test/data/sandbox/sampleData.xml";
    
//    @Test
//    public void duplicate_filepath() {
//        commandBox.runCommand("storage " + defaultPath);
//        assertResultMessage(StorageCommand.MESSAGE_DUPLICATE_PATH);
//    }
    
    @Test
    public void save_valid_path() {
        commandBox.runCommand("storage " + validPath);
        assertResultMessage(StorageCommand.MESSAGE_SUCCESS);
    }
    
    @Test
    public void save_invalid_path() {
        commandBox.runCommand("storage " + invalidPath);
        assertResultMessage(StorageCommand.MESSAGE_NO_XML);
    }
    
}
