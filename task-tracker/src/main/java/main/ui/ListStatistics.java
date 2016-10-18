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
	
	private static Logic logic;
	
	private static AnchorPane placeHolder;

	private VBox mainPane;

	public static ListStatistics load(Stage primaryStage, AnchorPane placeHolder,Logic logic) {
		ListStatistics listDisplay = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ListStatistics());
		listDisplay.logic = logic;
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
		
		ListStatistics panel = new ListStatistics();
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

		todaytasks.setText("No. of Task Due Today: ");
		tomorrowtasks.setText("No. of Task Due Tomorrow: ");
		eventtasks.setText("No. of Events: ");
		deadlinetasks.setText("No. of Deadlines: ");
		alltasks.setText("No. of Total: ");

	}
	
	private void updateStatistics(){
		alltasks.setText(""+logic.getFilteredTaskList().size());
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
