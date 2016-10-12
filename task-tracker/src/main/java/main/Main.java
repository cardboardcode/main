package main;
 
import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import main.commons.events.ui.ExitAppRequestEvent;
import main.commons.exceptions.DataConversionException;
import main.commons.util.ConfigUtil;
import main.commons.util.StringUtil;
import main.commons.core.Config;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.core.Version;
import main.logic.Logic;
import main.storage.Storage;
import main.storage.StorageManager;
import main.ui.MainWindow;
import main.ui.Ui;
import main.ui.UiManager;
import main.logic.LogicManager;

import com.google.common.eventbus.Subscribe;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import main.model.TaskTracker;
import main.model.Model;
import main.model.ModelManager;
import main.model.ReadOnlyTaskTracker;
import main.model.UserPrefs;
import main.model.task.ReadOnlyTask;


/**
 * The main entry point to the application.
 */
public class Main extends Application {
    private static final Logger logger = LogsCenter.getLogger(Main.class);

    public static final Version VERSION = new Version(1, 0, 0, true);

    protected Ui ui;
    protected Logic logic;
    protected Storage storage;
    protected Model model;
    protected Config config;
    protected UserPrefs userPrefs;

    public Main() {}

    @Override
    public void init() throws Exception {
        logger.info("=============================[ Initializing Task-Tracker ]===========================");
        super.init();

        config = initConfig(getApplicationParameter("config"));
        storage = new StorageManager(config.getTaskTrackerFilePath(), config.getUserPrefsFilePath());

        userPrefs = initPrefs(config);

        initLogging(config);

        model = initModelManager(storage, userPrefs);

        logic = new LogicManager(model, storage);

        ui = new UiManager(logic, config, userPrefs);

        initEventsCenter();
    }

    private String getApplicationParameter(String parameterName){
        Map<String, String> applicationParameters = getParameters().getNamed();
        return applicationParameters.get(parameterName);
    }

    private Model initModelManager(Storage storage, UserPrefs userPrefs) {
        Optional<ReadOnlyTaskTracker> taskTrackerOptional;
        ReadOnlyTaskTracker initialData;
        try {
        	taskTrackerOptional = storage.readTaskTracker();
            if(!taskTrackerOptional.isPresent()){
                logger.info("Data file not found. Will be starting with an empty AddressBook");
            }
            initialData = taskTrackerOptional.orElse(new TaskTracker());
            logger.info(initialData.getTaskList().size() + "");
        } catch (DataConversionException e) {
            logger.warning("Data file not in the correct format. Will be starting with an empty AddressBook");
            initialData = new TaskTracker();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty AddressBook");
            initialData = new TaskTracker();
        }

        return new ModelManager(new TaskTracker(initialData), userPrefs);
    }

    private void initLogging(Config config) {
        LogsCenter.init(config);
    }

    protected Config initConfig(String configFilePath) {
        Config initializedConfig;
        String configFilePathUsed;

        configFilePathUsed = Config.DEFAULT_CONFIG_FILE;

        if(configFilePath != null) {
            logger.info("Custom Config file specified " + configFilePath);
            configFilePathUsed = configFilePath;
        }

        logger.info("Using config file : " + configFilePathUsed);

        try {
            Optional<Config> configOptional = ConfigUtil.readConfig(configFilePathUsed);
            initializedConfig = configOptional.orElse(new Config());
        } catch (DataConversionException e) {
            logger.warning("Config file at " + configFilePathUsed + " is not in the correct format. " +
                    "Using default config properties");
            initializedConfig = new Config();
        }

        //Update config file in case it was missing to begin with or there are new/unused fields
        try {
            ConfigUtil.saveConfig(initializedConfig, configFilePathUsed);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }
        return initializedConfig;
    }

    protected UserPrefs initPrefs(Config config) {
        assert config != null;

        String prefsFilePath = config.getUserPrefsFilePath();
        logger.info("Using prefs file : " + prefsFilePath);

        UserPrefs initializedPrefs;
        try {
            Optional<UserPrefs> prefsOptional = storage.readUserPrefs();
            initializedPrefs = prefsOptional.orElse(new UserPrefs());
        } catch (DataConversionException e) {
            logger.warning("UserPrefs file at " + prefsFilePath + " is not in the correct format. " +
                    "Using default user prefs");
            initializedPrefs = new UserPrefs();
        } catch (IOException e) {
            logger.warning("Problem while reading from the file. . Will be starting with an empty AddressBook");
            initializedPrefs = new UserPrefs();
        }

        //Update prefs file in case it was missing to begin with or there are new/unused fields
        try {
            storage.saveUserPrefs(initializedPrefs);
        } catch (IOException e) {
            logger.warning("Failed to save config file : " + StringUtil.getDetails(e));
        }

        return initializedPrefs;
    }

    private void initEventsCenter() {
        EventsCenter.getInstance().registerHandler(this);
    }

    @Override
    public void start(Stage primaryStage) {
        logger.info("Starting Task-Tracker " + Main.VERSION);
        ui.start(primaryStage);
    }

    @Override
    public void stop() {
        logger.info("============================ [ Stopping Task-Tracker ] =============================");
        ui.stop();
        try {
            storage.saveUserPrefs(userPrefs);
        } catch (IOException e) {
            logger.severe("Failed to save preferences " + StringUtil.getDetails(e));
        }
        Platform.exit();
        System.exit(0);
    }

    @Subscribe
    public void handleExitAppRequestEvent(ExitAppRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        this.stop();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
//
//public class Main extends Application {
//
//    private Stage primaryStage;
//    private VBox rootLayout;
//
// 
//    @Override
//    public void start(Stage primaryStage) {
//        this.primaryStage = primaryStage;
//        this.primaryStage.setTitle("Task-Tracker d[T - T]b");
//        
//        //Set application icon
//        this.primaryStage.getIcons().add(new Image("file:resources/images/pp.png"));
//        initRootLayout();
//    }
//
//    /**
//     * Initializes the root layout.
//     */
//    public void initRootLayout() {
//        try {
//            // Load root layout from fxml file.
//            FXMLLoader loader = new FXMLLoader();
//            loader.setLocation(Main.class
//                    .getResource("ui/MainWindow.fxml"));
//            //It is casted to a Vbox because Vbox is its format.
//            rootLayout = (VBox) loader.load();
//
//            // Show the scene containing the root layout.
//            Scene scene = new Scene(rootLayout);
//            primaryStage.setScene(scene);
//            primaryStage.show();
//            
//            MainWindow mainWindow = loader.getController();
//            mainWindow.init();
//            mainWindow.setLogic(new Logic());
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    /**
//     * Returns the main stage.
//     * @return
//     */
//    public Stage getPrimaryStage() {
//        return primaryStage;
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//
//}
