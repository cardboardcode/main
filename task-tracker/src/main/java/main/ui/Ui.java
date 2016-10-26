package main.ui;

import javafx.stage.Stage;

/**
 * API of Ui component
 * All Ui classes which implements this interface must have a start and stop function
 * @author bey
 */
//@@author A0139422J
public interface Ui {

    /** Starts the UI (and the App).  */
    void start(Stage primaryStage);

    /** Stops the UI. */
    void stop();

}
