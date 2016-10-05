package main.ui;

import java.util.List;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import main.logic.Logic;

/**
 * Based codes mostly on Addressbook-Level3. Error present starting 2250 at night on 5th Oct
 * @author Hao Yun
 *
 */

public class MainWindow {
	
	private Logic logic;
		
    @FXML
    private TextArea outputConsole;

    @FXML
    private TextField commandInput;
    
    public MainWindow(){}
    
    public void init() {
        commandInput.requestFocus();
    }
    
    @FXML
    void onCommand(ActionEvent event) {
        try {
            String userCommandText = commandInput.getText();
            String result = logic.process(userCommandText);
            
            display(result);
            clearCommandInput();
        } catch (Exception e) {
            display(e.getMessage());
            throw new RuntimeException(e);
        }
    }
    
    /** Clears the command input box */
    private void clearCommandInput() {
        commandInput.setText("");
    }

    /** Clears the output display area */
    public void clearOutputConsole(){
        outputConsole.clear();
    }
    
    /**
     * Displays the given messages on the output display area, after formatting appropriately.
     */
    private void display(String messages) {
        outputConsole.setText(messages);
    }
    
    public void setLogic(Logic logic) {
        this.logic = logic;
    }
    
	
}
