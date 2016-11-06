//@@author A0139422J
package guitests;

import org.junit.Test;

import javafx.scene.input.KeyCode;
import main.commons.core.EventsCenter;
import main.commons.events.ui.KeyPressEvent;
import main.logic.command.FindCommand;

/**
 * Evaluates the find command when it searches for any tasks regarding a certain
 * keyword "friends"
 */
public class FindCommandTest extends TaskTrackerGuiTest {

	@Test
	public void find_oneKeyword_success() {
        // finds any tasks starting with the "frie"
	    String input = "find" + " frie";
	    
		commandBox.enterCommand(input);
		EventsCenter.getInstance().post(new KeyPressEvent(KeyCode.E, input));
		commandBox.pressEnter();
		
		assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS, 1));
	}
	
    @Test
    public void find_twoKeyword_success() {
        // finds any tasks starting with the "frie" or "b" 
        String input = "find" + " frie" + " b";
        
        commandBox.enterCommand(input);
        EventsCenter.getInstance().post(new KeyPressEvent(KeyCode.O, input));
        commandBox.pressEnter();
        
        assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS, 3));
    }
    
    @Test
    public void find_middleParam_success() {
        // finds any tasks starting with the "frie" or "b" 
        String input = "find" + " with";
        
        commandBox.enterCommand(input);
        EventsCenter.getInstance().post(new KeyPressEvent(KeyCode.H, input));
        commandBox.pressEnter();
        
        assertResultMessage(String.format(FindCommand.MESSAGE_SUCCESS, 1));
    }
}
