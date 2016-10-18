package main.ui;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import main.commons.util.FxViewUtil;
import javafx.fxml.FXML;

public class ListStatistics extends UiPart {
    public static final String RESULT_DISPLAY_ID = "listStatistics";
    
    @FXML
    private TextArea listStatisticsArea;

    private static final String FXML = "ListStatistics.fxml";
    
    @FXML
    private Label alltasks;
    
    @FXML
    private Label eventtasks;
    
    @FXML
    private Label floatingtasks;
    
    @FXML
    private ImageView image;
    
    private AnchorPane placeHolder;

    private AnchorPane mainPane;

    public static ListStatistics load(Stage primaryStage, AnchorPane placeHolder) {
        ListStatistics statusBar = UiPartLoader.loadUiPart(primaryStage, placeHolder, new ListStatistics());
        statusBar.configure();
        return statusBar;
    }

    public void configure() {
        image = new ImageView(new Image("file:/images/pp.png"));
//        listStatisticsArea.setEditable(false);
//        listStatisticsArea.setText("WHY?");
//        alltasks.setText("STUB");
//        eventtasks.setText("STUB");
//        floatingtasks.setText("STUB");
//        FxViewUtil.applyAnchorBoundaryParameters(listStatisticsArea, 0.0, 0.0, 0.0, 0.0);
//        mainPane.getChildren().add(listStatisticsArea);
//        FxViewUtil.applyAnchorBoundaryParameters(mainPane, 0.0, 0.0, 0.0, 0.0);
//        placeHolder.getChildren().add(mainPane);
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
