package main.logic.parser;

import main.commons.core.LogsCenter;
import main.commons.core.Messages;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
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
    private static final Pattern EDIT_FORMAT = Pattern.compile("(?<index>\\d)(?<task>.*)");
    private static final Logger logger = LogsCenter.getLogger(MainParser.class);

       
    public MainParser() {}
    
    public Command parse(String input){
        
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        
        if (!matcher.matches()) {
            return new IncorrectCommand(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
        
        logger.fine("command word: " + commandWord);

        if (!ReferenceList.commandsDictionary.containsKey(commandWord)) {
            return new IncorrectCommand(Messages.MESSAGE_UNKNOWN_COMMAND);
        }

        switch (ReferenceList.commandsDictionary.get(commandWord)) {
            case AddCommand.COMMAND_WORD:
                return prepareAdd(task);
            case EditCommand.COMMAND_WORD:
                return prepareEdit(task);
            case DeleteCommand.COMMAND_WORD:
                return prepareDelete(task);
            case ListCommand.COMMAND_WORD:
                return new ListCommand();
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            case "finish":
                return prepareDelete(task);
            default: 
                return new IncorrectCommand(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
           
    }
    
    /*
     * checks whether task has a deadline, is an event, or is floating,
     * and uses the appropriate constructors accordingly.
     *         String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
     * @return an AddCommand Object
     */    
    public Command prepareAdd(String task) {

        Pair<String,List<Date>> info = TimeParser.extractTime(task.trim());
        List<Date> dates = info.getValue();
        String description = info.getKey();
        if (description.trim().equals("")) {
            return new IncorrectCommand(Messages.MESSAGE_EMPTY_DESCRIPTION);
        }
        
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
    
    public Command prepareEdit(String input) {
        final Matcher edit_matcher = EDIT_FORMAT.matcher(input.trim());
        
        if (!edit_matcher.matches()) {
            return new IncorrectCommand(EditCommand.MESSAGE_USAGE);
        }
        
        // TODO let index start from 1
        int index = Integer.valueOf(edit_matcher.group("index"));
        String task = edit_matcher.group("task");
      
        Pair<String,List<Date>> info = TimeParser.extractTime(task.trim());

        List<Date> dates = info.getValue();
        String description = info.getKey();
        
        if (description.trim() == "") {
            return new IncorrectCommand(Messages.MESSAGE_EMPTY_DESCRIPTION);
        }
        
        if (dates.isEmpty()) {
            return new EditCommand(index, new Task(description));
        }
        else if (dates.size() == 1) { 
            return new EditCommand(index, new Task(description,dates.get(0)));
        }
        // compare dates if there are 2 dates
        else {
            if (dates.get(0).before(dates.get(1)))
                return new EditCommand(index, new Task(description,dates.get(0),dates.get(1)));
            else 
                return new EditCommand(index, new Task(description,dates.get(1),dates.get(0)));
        }
     
    }
    
    public Command prepareDelete(String input) {
        
        int index;
        
        try {
            index = Integer.valueOf(input.trim());
        } catch (NumberFormatException e) {
            return new IncorrectCommand(DeleteCommand.MESSAGE_USAGE);
        }

        return new DeleteCommand(index);
    }
    
}
