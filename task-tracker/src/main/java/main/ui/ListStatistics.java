package main.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.commons.util.AppUtil;
import main.commons.util.FxViewUtil;
import main.logic.Logic;
import main.model.Model;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;

public class ListStatistics extends UiPart {

	private static final String FXML = "ListStatistics.fxml";

	@FXML
	private Label alltasks;

	@FXML
	private Label eventtasks;

	@FXML
	private Label tomorrowtasks;

	@FXML
	private Label deadlinetasks;

	@FXML
	private Label todaytasks;

	@FXML
	private ImageView image;
	
	private Model model;
	
	private static Logic logic;
	
	private AnchorPane placeHolder;

	private static VBox mainPane;
	
	private static ListStatistics listDisplay;

	public static ListStatistics load(Stage primaryStage, AnchorPane placeHolder,Logic logic) {
		listDisplay = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ListStatistics()); 
		ListStatistics.logic = logic;
//		listDisplay.model = logic.getModel();
		listDisplay.configure();
		return listDisplay;
	}

	public ListStatistics() {
		todaytasks = new Label();
		tomorrowtasks = new Label();
		eventtasks = new Label();
		deadlinetasks = new Label();
		alltasks = new Label();
	}

	public void configure() {
		
		listDisplay = new ListStatistics();
		mainPane = new VBox();
		
		initialize();
		setListIcon();
		mainPane.getChildren().addAll(image, todaytasks, tomorrowtasks, eventtasks, deadlinetasks,alltasks);
		mainPane.setSpacing(30.0);
		mainPane.setPadding(new Insets(30.0, 0.0, 30.0, 30.0));
		placeHolder.getChildren().add(mainPane);
		
	}

	private void setListIcon() {
		image = new ImageView(AppUtil.getImage("/images/statistics.png"));
	}

	private void initialize() {

//		todaytasks.setText("No. of Task Due Today: " + model.getNumToday());
//		tomorrowtasks.setText("No. of Task Due Tomorrow: " + model.getNumTmr());
//		eventtasks.setText("No. of Events: " + model.getNumEvents());
//		deadlinetasks.setText("No. of Deadlines: " + model.getNumDeadline());
		todaytasks.setText("No. of Task Due Today: ");
		tomorrowtasks.setText("No. of Task Due Tomorrow: ");
		eventtasks.setText("No. of Events: ");
		deadlinetasks.setText("No. of Deadlines: ");
		alltasks.setText("Total: " + logic.getFilteredTaskList().size());

	}
	
	public static void updateStatistics(){
//		listDisplay.getAllTasksLabel().setText("No. of Total: "+listDisplay.getLogic().getFilteredTaskList().size());
		System.out.println(logic.getFilteredTaskList().size());
		Label newalltasks = new Label();
		newalltasks.setText("Total: "+logic.getFilteredTaskList().size());
		System.out.println(mainPane==null);
		mainPane.getChildren().set(5, newalltasks);
	}
	
	public Label getAllTasksLabel(){
		return alltasks;
	}
		
	@Override
	public void setNode(Node node) {
		mainPane = (VBox) node;
	}

	@Override
	public void setPlaceholder(AnchorPane placeholder) {
		this.placeHolder = placeholder;
	}

	@Override
	public String getFxmlPath() {
		return FXML;
	}
}
