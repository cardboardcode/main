//@@author A0139422J
package guitests.guihandles;

import guitests.GuiRobot;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import main.TestMain;

/**
 * A handle to the Command Box in the GUI.
 */
public class CommandBoxHandle extends GuiHandle{

    private static final String COMMAND_INPUT_FIELD_ID = "#commandTextField";

    public CommandBoxHandle(GuiRobot guiRobot, Stage primaryStage, String stageTitle) {
        super(guiRobot, primaryStage, stageTitle);
    }

    public void enterCommand(String command) {
        setTextField(COMMAND_INPUT_FIELD_ID, command);
    }

    public String getCommandInput() {
        return getTextFieldText(COMMAND_INPUT_FIELD_ID);
    }

    /**
     * Enters the given command in the Command Box and presses enter.
     */
    public void runCommand(String command) {
        enterCommand(command);
        pressEnter();
        guiRobot.sleep(200); //Give time for the command to take effect
    }
    
    /**
     * Enters the given command in the Command Box and does not enter.
     */
    public void runStaticCommand(String command) {
        enterCommand(command);
        guiRobot.sleep(500); //Give time for the command to take effect
    }
    
    public HelpWindowHandle runHelpCommand() {
        enterCommand("help");
        pressEnter();
        return new HelpWindowHandle(guiRobot, primaryStage);
    }
    
    public CommandBoxHandle minimizeWindow(){
    	useEscKey();
    	return new CommandBoxHandle(guiRobot, primaryStage, TestMain.APP_TITLE);
    }

	private void useEscKey() {
		guiRobot.push(KeyCode.ESCAPE);
		guiRobot.sleep(5000);
		
	}

}
