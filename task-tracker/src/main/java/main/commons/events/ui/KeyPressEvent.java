//@@author A0144132W
package main.commons.events.ui;

import javafx.scene.input.KeyCode;
import main.commons.events.BaseEvent;

public class KeyPressEvent extends BaseEvent{
    private String input;
    private KeyCode button;

    public KeyPressEvent(KeyCode button, String input) {
        this.button = button;
        this.input = input;
    }
    
    public KeyCode getKey() {
        return button;
    }
    
    public String getInput() {
        return input;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    

}
