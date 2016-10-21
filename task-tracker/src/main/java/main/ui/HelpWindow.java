package main.ui;



import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import main.commons.util.FxViewUtil;
import main.Main;
import main.commons.core.LogsCenter;
import java.util.logging.Logger;
import javafx.fxml.FXML;

/**
 * Controller for a help page
 * "person" keyword check done
 * "addressbook" keyword check done
 * @author bey
 */
public class HelpWindow extends UiPart {

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String ICON = "/images/pp.png";
    private static final String FXML = "HelpWindow.fxml";
    private static final String TITLE = "Help";

    private AnchorPane mainPane;

    private Stage dialogStage;

    public static HelpWindow load(Stage primaryStage) {
        logger.fine("Showing help page about the application.");
        HelpWindow helpWindow = UiPartLoader.loadUiPart(primaryStage, new HelpWindow());
        helpWindow.configure();
        return helpWindow;
    }
   
    @Override
    public void setNode(Node node) {
        mainPane = (AnchorPane) node;
    }

    @Override
    public String getFxmlPath() {
        return FXML;
    }

    private void configure(){
        Scene scene = new Scene(mainPane);
        //Null passed as the parent stage to make it non-modal.
        dialogStage = createDialogStage(TITLE, null, scene);
        dialogStage.setMaximized(true); //TODO: set a more appropriate initial size
        setIcon(dialogStage, ICON);

        WebView browser = new WebView();
        String url  = Main.class.getResource("/html/help.html").toExternalForm();
        browser.getEngine().load(url);
        FxViewUtil.applyAnchorBoundaryParameters(browser, 0.0, 0.0, 0.0, 0.0);
        mainPane.getChildren().add(browser);
    }

    public void show() {
        dialogStage.show();
    } 
    
    public void closeHelpWindow(){
    	dialogStage.close();
    }
 
}
