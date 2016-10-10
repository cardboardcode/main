package main.logic;

import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.logic.command.Command;
import main.logic.command.CommandResult;
import main.logic.parser.MainParser;
import main.model.model.Model;
import main.storage.Storage;

import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final MainParser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new MainParser();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parse(commandText);
        command.setData(model);
        return command.execute();

    }

//    @Override
//    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
//        return model.getFilteredPersonList();
//    }
}
