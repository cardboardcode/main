//@@author A0139422J
package main.ui;

import main.commons.core.Config;
import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.commons.core.Config;
import main.commons.core.GuiSettings;
import main.commons.events.ui.ExitAppRequestEvent;
import main.commons.util.FxViewUtil;
import main.logic.Logic;
import main.model.UserPrefs;
import main.model.task.ReadOnlyTask;

/**
 * Instantiates all the individual components for the Gui and interacts with
 * UiManager - CommandBox, HelpWindow, StatusBarFooter, TaskCard, TaskListPanel
 * "person" keyword check done "addressbook" keyword check done
 * 
 * @param AnchorPane
 *            commandBoxPlaceholder
 * @param MenuItem
 *            helpMenuItem
 * @param AnchorPane
 *            taskListPanelPlaceholder
 * @param AnchorPane
 *            resultDisplayPlaceholder
 * @param AnchorPane
 *            statusbarPlaceholder
 * @author bey
 */

public class MainWindow extends UiPart {

	private static final String ICON = "/images/pp.png";
	private static final String FXML = "MainWindow.fxml";
	public static final int MIN_HEIGHT = 620;
	public static final int MIN_WIDTH = 450;

	private Logic logic;

	// Independent Ui parts residing in this Ui container
	private TaskListPanel taskListPanel;
	private ResultDisplay resultDisplay;
	private StatusBarFooter statusBarFooter;
	private CommandBox commandBox;
	private ListStatistics listStatistics;
	private HelpWindow helpWindow;
	private Config config;
	private UserPrefs userPrefs;

	// Handles to elements of this Ui container
	private VBox rootLayout;
	private Scene scene;

	private String taskTrackerName;

	@FXML
	private SplitPane splitpane;

	@FXML
	private AnchorPane commandBoxPlaceholder;

	@FXML
	private AnchorPane taskListPanelPlaceholder;

	@FXML
	private AnchorPane resultDisplayPlaceholder;

	@FXML
	private AnchorPane statusbarPlaceholder;

	@FXML
	private AnchorPane listStatisticsPlaceholder;

	public static final KeyCodeCombination KEY_MINMAX = new KeyCodeCombination(KeyCode.M, KeyCodeCombination.ALT_DOWN);
	public static int listPointer = 0;

	public MainWindow() {
		super();
	}

	@Override
	public void setNode(Node node) {
		rootLayout = (VBox) node;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}

	public static MainWindow load(Stage primaryStage, Config config, UserPrefs prefs, Logic logic) {

		MainWindow mainWindow = UiPartLoader.loadUiPart(primaryStage, new MainWindow());
		mainWindow.configure(config.getAppTitle(), config.getTaskTrackerName(), config, prefs, logic);
		return mainWindow;
	}

	private void configure(String appTitle, String taskTrackerName, Config config, UserPrefs prefs, Logic logic) {

		// Set dependencies
		this.logic = logic;
		this.taskTrackerName = taskTrackerName;
		this.config = config;
		this.userPrefs = prefs;

		// Configure the UI
		setTitle(appTitle);
		setIcon(ICON);
		setWindowMinSize();
		setWindowDefaultSize(prefs);
		scene = new Scene(rootLayout);
		primaryStage.setScene(scene);

		// setAccelerators();
	}

	// private void setAccelerators() {
	// helpMenuItem.setAccelerator(KeyCombination.valueOf("F1"));
	// }

	void fillInnerParts() {
		taskListPanel = TaskListPanel.load(primaryStage, getTaskListPlaceholder(), logic.getFilteredTaskList());
		resultDisplay = ResultDisplay.load(primaryStage, getResultDisplayPlaceholder());
		statusBarFooter = StatusBarFooter.load(primaryStage, getStatusbarPlaceholder(),
				config.getTaskTrackerFilePath());
		commandBox = CommandBox.load(primaryStage, getCommandBoxPlaceholder(), resultDisplay, logic);
		listStatistics = ListStatistics.load(primaryStage, getListStatisticsPlaceholder(), logic);
		setInitialInputFocus();
		handleAllEvents();
		FxViewUtil.applyAnchorBoundaryParameters(rootLayout, 0.0, 0.0, 0.0, 0.0);
		splitpane.maxWidthProperty().multiply(0.5);
	}

	@FXML
	private void setInitialInputFocus() {
		commandBox.getCommandBoxTextField().requestFocus();
	}

	private AnchorPane getCommandBoxPlaceholder() {
		return commandBoxPlaceholder;
	}

	private AnchorPane getStatusbarPlaceholder() {
		return statusbarPlaceholder;
	}

	private AnchorPane getResultDisplayPlaceholder() {
		return resultDisplayPlaceholder;
	}

	public AnchorPane getTaskListPlaceholder() {
		return taskListPanelPlaceholder;
	}

	public AnchorPane getListStatisticsPlaceholder() {
		return listStatisticsPlaceholder;
	}

	public void hide() {
		primaryStage.hide();
	}

	private void setTitle(String appTitle) {
		primaryStage.setTitle(appTitle);
	}

	/**
	 * Sets the default size based on user preferences.
	 */
	protected void setWindowDefaultSize(UserPrefs prefs) {
		primaryStage.setHeight(prefs.getGuiSettings().getWindowHeight());
		primaryStage.setWidth(prefs.getGuiSettings().getWindowWidth());
		if (prefs.getGuiSettings().getWindowCoordinates() != null) {
			primaryStage.setX(prefs.getGuiSettings().getWindowCoordinates().getX());
			primaryStage.setY(prefs.getGuiSettings().getWindowCoordinates().getY());
		}
	}

	private void setWindowMinSize() {
		// primaryStage.setMinHeight(MIN_HEIGHT);
		// primaryStage.setMaximized(true);
	}

	/**
	 * Returns the current size and the position of the main Window.
	 */
	public GuiSettings getCurrentGuiSetting() {
		return new GuiSettings(primaryStage.getWidth(), primaryStage.getHeight(), (int) primaryStage.getX(),
				(int) primaryStage.getY());
	}

	public void handleHelp() {
		helpWindow = HelpWindow.load(primaryStage);
		helpWindow.show();
	}

	public void closeHelpWindow() {
		if (helpWindow != null)
			helpWindow.closeHelpWindow();
	}

	public void show() {
		primaryStage.show();
	}

	/**
	 * Closes the application.
	 */
	@FXML
	private void handleExit() {
		raise(new ExitAppRequestEvent());
	}

	public TaskListPanel getTaskListPanel() {
		return this.taskListPanel;
	}

	public void handleWindowResize() {
		rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (KEY_MINMAX.match(event)) {
				resizeWindow();
			}
		});
	}

	private void resizeWindow() {
		if (primaryStage.isMaximized()) {
			primaryStage.setMaximized(false);
		} else {
			primaryStage.setMaximized(true);
		}
	}

	public void handleAllEvents() {
		handleWindowResize();
		handleTaskListScrolling();
	}

	private void handleTaskListScrolling() {
		ListView<ReadOnlyTask> scrollList = taskListPanel.getTaskListView();
		int max = TaskListPanel.getCurrentTaskListSize();
		System.out.println("This is maxListPointer:  " + max);
		handlePageUp(scrollList, max);
		handlePageDown(scrollList, max);
	}

	public void handlePageDown(ListView<ReadOnlyTask> scrollList, int max) {
		
		rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (event.getCode() == KeyCode.PAGE_DOWN) {
				if ((listPointer + 1) > max)
					listPointer++;
				else {
					listPointer = 0;
				}

				scrollList.scrollTo(listPointer);
			}
		});

	}

	public void handlePageUp(ListView<ReadOnlyTask> scrollList, int max) {

		rootLayout.addEventFilter(KeyEvent.KEY_PRESSED, (KeyEvent event) -> {
			if (event.getCode() == KeyCode.PAGE_UP) {
				if ((listPointer - 1) < 0)
					listPointer--;
				else {
					listPointer = max;
				}
				scrollList.scrollTo(listPointer);
			}
		});
	}

}
