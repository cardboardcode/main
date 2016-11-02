//@@author A0144132W
package main.logic.command;

import main.commons.core.EventsCenter;
import main.commons.core.Messages;
import main.commons.events.model.ChangeSortFilterEvent;

public class SortCommand extends Command {
    
    public static final String COMMAND_WORD = "sort";
    public static final String MESSAGE_USAGE = COMMAND_WORD 
            + "Parameters: <date | name> \n"
            + "Parameter can only be 'date' or 'name' \n"
            + "Eg: " + COMMAND_WORD + " date";
    public static final String MESSAGE_SUCCESS = "Sorting by %1$s";
    
    private String param;
    public SortCommand(String param) {
        this.param = param;
    }

    @Override
    public CommandResult execute() {
        assert (param.equals("date") || param.equals("name"));

        EventsCenter.getInstance().post(new ChangeSortFilterEvent(param));
        return new CommandResult(String.format(MESSAGE_SUCCESS, param));
    }
    

}
