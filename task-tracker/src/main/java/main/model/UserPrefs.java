//@@author A0139750B
package main.model;

import main.commons.core.GuiSettings;
import java.util.Objects;

/**
 * Represents User's preferences.
 */
public class UserPrefs {

    public GuiSettings guiSettings;
    public int colourPointer;

    public GuiSettings getGuiSettings() {
        return guiSettings == null ? new GuiSettings() : guiSettings;
    }

    public void updateLastUsedGuiSetting(GuiSettings guiSettings) {
        this.guiSettings = guiSettings;
    }

    public UserPrefs(){
        this.setGuiSettings(900, 500, 0, 0);
    }

    public void setGuiSettings(double width, double height, int x, int y) {
        guiSettings = new GuiSettings(width, height, x, y);
    }
    
    //@@author A0142686X
    /**
     * Updates the colour pointer index to the selected colour theme
     */
    public void updateColourPointer(int value) {
        this.colourPointer = value;
    }
    
    public int getColourPointer() {
        return this.colourPointer;
    }
    
  //@@author A0139750B
    @Override
    public boolean equals(Object other) {
        if (other == this){
            return true;
        }
        if (!(other instanceof UserPrefs)){ //this handles null as well.
            return false;
        }

        UserPrefs o = (UserPrefs)other;

        return Objects.equals(guiSettings, o.guiSettings);
    }

    @Override
    public int hashCode() {
        return Objects.hash(guiSettings);
    }

    @Override
    public String toString(){
        return guiSettings.toString();
    }

}
