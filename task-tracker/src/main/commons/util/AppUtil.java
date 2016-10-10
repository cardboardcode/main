package main.commons.util;

import javafx.scene.image.Image;
import main.Main;

/**
 * A container for App specific utility functions
 */
public class AppUtil {

    public static Image getImage(String imagePath) {
        assert imagePath != null;
        return new Image(Main.class.getResourceAsStream(imagePath));
    }

}
