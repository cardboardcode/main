package main;
 
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.logic.Logic;
import main.ui.MainWindow;



public class Main extends Application {

    private Stage primaryStage;
    private VBox rootLayout;

 
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Task-Tracker d[T - T]b");
        
        //Set application icon
        this.primaryStage.getIcons().add(new Image("file:resources/images/pp.jpg"));
        initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout() {
        try {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(Main.class
                    .getResource("ui/MainWindow.fxml"));
            //It is casted to a Vbox because Vbox is its format.
            rootLayout = (VBox) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
            
            MainWindow mainWindow = loader.getController();
            mainWindow.init();
            mainWindow.setLogic(new Logic());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns the main stage.
     * @return
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }

}
