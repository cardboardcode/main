package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import main.TestMain;


/**
 * Provides a handle for the main GUI.
 */
//@@author A0139422J
public class MainGuiHandle extends GuiHandle {

    public MainGuiHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestMain.APP_TITLE);
    }

    public TaskListPanelHandle getPersonListPanel() {
        return new TaskListPanelHandle(guiRobot, primaryStage);
    }

    public ResultDisplayHandle getResultDisplay() {
        return new ResultDisplayHandle(guiRobot, primaryStage);
    }

    public CommandBoxHandle getCommandBox() {
        return new CommandBoxHandle(guiRobot, primaryStage, TestMain.APP_TITLE);
    }

    public MainMenuHandle getMainMenu() {
        return new MainMenuHandle(guiRobot, primaryStage);
    }

}
