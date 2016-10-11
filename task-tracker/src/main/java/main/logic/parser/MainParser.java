package main.logic.parser;

import main.commons.core.Messages;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.model.task.Task;
import main.logic.command.AddCommand;
import main.logic.command.Command;
import main.logic.command.DeleteCommand;
import main.logic.command.EditCommand;
import main.logic.command.ExitCommand;
import main.logic.command.HelpCommand;
import main.logic.command.IncorrectCommand;
import main.logic.command.ListCommand;
import javafx.util.Pair;

public class MainParser {
    
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<task>.*)");
       
    public MainParser() {}
    
    public Command parse(String input){
        
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        
        if (!matcher.matches()) {
            return new IncorrectCommand(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
        
        logger.info("command detected: " + commandWord);
        
        switch (ReferenceList.commandsDictionary.get(commandWord)) {
            case AddCommand.COMMAND_WORD:
                return prepareAdd(task);
            case EditCommand.COMMAND_WORD:
                return prepareEdit(task);
            case DeleteCommand.COMMAND_WORD:
                return new DeleteCommand(Integer.valueOf(task));
            case ListCommand.COMMAND_WORD:
                return new ListCommand();
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            default: 
                return new IncorrectCommand(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
           
    }
    
    /*
     * checks whether task has a deadline, is an event, or is floating,
     * and uses the appropriate constructors accordingly.
     * 
     * @return an AddCommand Object
     */    
    public Command prepareAdd(String task) {

        Pair<String,List<Date>> info = timeParser.extractTime(task);
        List<Date> dates = info.getValue();
        String description = info.getKey();
        
        if (dates.isEmpty()) {
            return new AddCommand(new Task(description));
        }
        else if (dates.size() == 1) { 
            return new AddCommand(new Task(description,dates.get(0)));
        }
        // compare dates if there are 2 dates
        else {
            if (dates.get(0).before(dates.get(1)))
                return new AddCommand(new Task(description,dates.get(0),dates.get(1)));
            else 
                return new AddCommand(new Task(description,dates.get(1),dates.get(0)));
        }
                      
    }
    
    //dummy
    // not ready
    public Command prepareEdit(String task) {
//        return new EditCommand(1);
        return new IncorrectCommand(Messages.MESSAGE_UNKNOWN_COMMAND);
    }

    
}
