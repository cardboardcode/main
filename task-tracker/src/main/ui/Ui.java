package main.ui;

import javafx.stage.Stage;

/**
 * API of UI component
 * @author bey
 */
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage);

    /** Stops the UI. */
    void stop();

}