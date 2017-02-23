//@@author A0144132W
package main.logic;

import java.util.logging.Logger;

import javafx.collections.ObservableList;
import main.commons.core.ComponentManager;
import main.commons.core.LogsCenter;
import main.logic.autocomplete.AutoComplete;
import main.logic.command.Command;
import main.logic.command.CommandResult;
import main.logic.parser.MainParser;
import main.model.Model;
import main.model.task.ReadOnlyTask;

/**
 * The main LogicManager of the app.
 */
public class LogicManager extends ComponentManager implements Logic {
	private final Logger logger = LogsCenter.getLogger(LogicManager.class);

	private final Model model;
	private final MainParser parser;
	private final AutoComplete autoComplete;

	public LogicManager(Model model) {
		this.model = model;
		this.parser = new MainParser();
		this.autoComplete = new AutoComplete(model);
	}

	@Override
	public CommandResult execute(String commandText) {
		logger.info("----------------[USER COMMAND][" + commandText + "]");
		long timeBefore = System.currentTimeMillis();
		
		Command command = parser.parse(commandText);
		command.setData(model);
		
		long timeAfter = System.currentTimeMillis();
		logger.info("Time taken for command to execute is: " + (timeAfter - timeBefore) + " milliseconds. ");
		
		return command.execute();

	}

	@Override
	public ObservableList<ReadOnlyTask> getFilteredTaskList() {
		return model.getFilteredTaskList();
	}

	@Override
	public int getNumToday() {
		return model.getNumToday();
	}

	@Override
	public int getNumTmr() {
		return model.getNumTmr();
	}

	@Override
	public int getNumEvent() {
		return model.getNumEvent();
	}

	@Override
	public int getNumDeadline() {
		return model.getNumDeadline();
	}

	@Override
	public int getNumFloating() {
		return model.getNumFloating();
	}

	@Override
	public int getTotalNum() {
		return model.getTotalNum();
	}

	@Override
	public int getNumOverdue() {
		return model.getNumOverdue();
	}
}
