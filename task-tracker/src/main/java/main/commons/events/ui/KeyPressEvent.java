//@@author A0144132W
package main.commons.events.ui;

import javafx.scene.input.KeyCode;
import main.commons.events.BaseEvent;

public class KeyPressEvent extends BaseEvent{
    
    private KeyCode button;
    
    public KeyPressEvent(KeyCode button) {
        this.button = button;
    }
    
    public KeyCode getKey() {
        return button;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }
    

}
