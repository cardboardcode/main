//@@author A0139422J
package guitests;

import org.junit.Test;

import main.logic.command.FindCommand;

public class FindCommandTest extends TaskTrackerGuiTest{
	
	@Test
	public void find(){
		//Find command does not run on GuiTests
		commandBox.runCommand("find" + " friends");
		assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS,6));
	}
}
