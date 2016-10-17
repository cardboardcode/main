package main.ui;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Display the gif file collection on T-T bot which is a glorified version of a help command
 * "person" keyword check done
 * "addressbook" keyword check done
 * @author bey
 *
 */

public class TTBot extends UiPart{
	private static final String FXML = "TTBot.fxml";
	
	@FXML
	private ImageView botAssistDisplay;
	
	private AnchorPane panel;
	

	public static TTBot load(Stage primaryStage, AnchorPane ttBotPlaceholder) {
		TTBot botAssist = UiPartLoader.loadUiPart(primaryStage, ttBotPlaceholder, new TTBot());
		botAssist.configure();
		return botAssist;
	}
	
	private void configure() {
//		botAssistDisplay.setImage(new Image("file:resources/images/pp.png"));
	}
	
	@FXML
    public void initialize() {
//		botAssistDisplay.setImage(new Image("file:resources/images/pp.png"));
        
    }

	@Override
    public void setNode(Node node) {
        panel = (AnchorPane) node;
    }
	
    @Override
    public String getFxmlPath() {
        return FXML;
    }
	
}
