package main.logic.parser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.commons.core.Messages;
import main.data.Task;
import main.logic.command.AddCommand;
import main.logic.command.Command;
import main.logic.command.HelpCommand;
import main.logic.command.ListCommand;
import org.apache.commons.cli.*;

import javafx.util.Pair;

public class MainParser {
    
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<task>.*)");
       
    public MainParser() {}
    
    public Command parse(String input){
        
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        
        if (!matcher.matches()) {
            return new HelpCommand("wrong_format");
        }
        String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
        
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
            default: 
                return new HelpCommand("unknown_command");
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
            return new AddCommand(description);
        }
        else if (dates.size() == 1) { 
            return new AddCommand(description,dates.get(0));
        }
        // compare dates if there are 2 dates
        else {
            if (dates.get(0).before(dates.get(1)))
                return new AddCommand(description,dates.get(0),dates.get(1));
            else 
                return new AddCommand(description,dates.get(1),dates.get(0));
        }
                      
    }
    
    //dummy
    public Command prepareEdit(String task) {
        return new Command();
    }

    //dummy
    public Command prepareDelete(String task) {
        return new Command();
    }
}
