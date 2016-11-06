//@@author A0142686X
package guitests;

import org.junit.Test;
import main.logic.command.StorageCommand;

/**
 * Evaluates storage command by saving it to a valid file path and an invalid
 * file path
 */
public class StorageCommandTest extends TaskTrackerGuiTest {    
    String FILEPATH_VALID = "src/test/data/sandbox/newfile.xml";
    String FILEPATH_INVALID = "src/test/data/sandbox/textfile.txt"; 
    
    @Test
    public void save_validpath() {
        commandBox.runCommand("storage " + FILEPATH_VALID);
        assertResultMessage(String.format(StorageCommand.MESSAGE_SUCCESS, FILEPATH_VALID));
    }
    
    @Test
    public void save_invalidpath() {
        commandBox.runCommand("storage " + FILEPATH_INVALID);
        assertResultMessage(StorageCommand.MESSAGE_NO_XML);
    }    
}
