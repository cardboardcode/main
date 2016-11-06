package guitests;

import org.junit.Test;
import main.logic.command.StorageCommand;

/**
 * Evaluates storage command by saving it to a valid file path and an invalid
 * file path
 */
public class StorageCommandTest extends TaskTrackerGuiTest {

	String validPath = "src/test/data/sandbox/newfile.xml";
	String invalidPath = "src/test/data/sandbox/textfile.txt";
	String defaultPath = "src/test/data/sandbox/sampleData.xml";

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
