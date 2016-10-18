package main.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.commons.util.FxViewUtil;
import main.logic.Logic;
import javafx.fxml.FXML;

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

//	@FXML
//	private ImageView image;
	private Logic logic;
	
	private AnchorPane placeHolder;

	private AnchorPane mainPane;

	public static ListStatistics load(Stage primaryStage, AnchorPane placeHolder,Logic logic) {
		ListStatistics listDisplay = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ListStatistics());
		listDisplay.configure(logic);
		return listDisplay;
	}

	public ListStatistics() {
		todaytasks = new Label();
		tomorrowtasks = new Label();
		eventtasks = new Label();
		deadlinetasks = new Label();
		alltasks = new Label();
	}

	public void configure(Logic logic) {
		ListStatistics panel = new ListStatistics();
		mainPane = new AnchorPane();
		this.logic = logic;
		initialize();
		addAllChildren();
		placeHolder.getChildren().add(mainPane);
		
	}

	private void addAllChildren() {
		mainPane.getChildren().add(todaytasks);
		mainPane.getChildren().add(tomorrowtasks);
		mainPane.getChildren().add(eventtasks);
		mainPane.getChildren().add(deadlinetasks);
		mainPane.getChildren().add(alltasks);
	}

	private void initialize() {
		todaytasks.setText("STUB");
		tomorrowtasks.setText("STUB");
		eventtasks.setText("STUB");
		deadlinetasks.setText("STUB");
		alltasks.setText("STUB");
	}

	@Override
	public void setNode(Node node) {
		mainPane = (AnchorPane) node;
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
