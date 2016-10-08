package main.logic.parser;

import javafx.collections.ObservableList;
import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.logic.command.Command;
import main.logic.command.CommandResult;
import main.logic.parser.Parser;
import main.model.Model;
import main.storage.Storage;

import java.util.logging.Logger;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
    private final Logger logger = LogsCenter.getLogger(LogicManager.class);

    private final Model model;
    private final Parser parser;

    public LogicManager(Model model, Storage storage) {
        this.model = model;
        this.parser = new Parser();
    }

    @Override
    public CommandResult execute(String commandText) {
        logger.info("----------------[USER COMMAND][" + commandText + "]");
        Command command = parser.parseCommand(commandText);
        command.setData(model);
        return command.execute();
    }

//    @Override
//    public ObservableList<ReadOnlyPerson> getFilteredPersonList() {
//        return model.getFilteredPersonList();
//    }
}
