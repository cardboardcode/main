package main.logic.parser;

import main.commons.core.LogsCenter;
import main.commons.core.Messages;
import main.commons.exceptions.MultiplePriorityException;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import main.model.task.Task;
import main.logic.command.AddCommand;
import main.logic.command.Command;
import main.logic.command.DeleteCommand;
import main.logic.command.EditCommand;
import main.logic.command.ExitCommand;
import main.logic.command.HelpCommand;
import main.logic.command.IncorrectCommand;
import main.logic.command.ListCommand;

public class MainParser {
    
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<task>.*)");
    private static final Pattern EDIT_FORMAT = Pattern.compile("(?<index>\\d)(?<task>.*)");
//    private static final Pattern PRIORITY_FORMAT = Pattern.compile("(?<task>[^/]+)(?<priority>(-h|-m|-l)?)");
    private static final Logger logger = LogsCenter.getLogger(MainParser.class);

       
    public MainParser() {}
    
    public Command parse(String input){
        
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        
        if (!matcher.matches()) {
            return commandIncorrect(Messages.MESSAGE_INVALID_COMMAND_FORMAT);
        }
        String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
        
        logger.fine("command word: " + commandWord);

        if (!ReferenceList.commandsDictionary.containsKey(commandWord.toLowerCase())) {
            return commandIncorrect(Messages.MESSAGE_UNKNOWN_COMMAND);
        }

        switch (ReferenceList.commandsDictionary.get(commandWord.toLowerCase())) {
            case AddCommand.COMMAND_WORD:
                return prepareAdd(task);
            case EditCommand.COMMAND_WORD:
                return prepareEdit(task);
            case DeleteCommand.COMMAND_WORD:
                return prepareDelete(task);
            case ListCommand.COMMAND_WORD:
                return prepareList(task);
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand();
            case ExitCommand.COMMAND_WORD:
                return new ExitCommand();
            case "finish":
                return prepareDelete(task);
            default: 
                return commandIncorrect(Messages.MESSAGE_UNKNOWN_COMMAND);
        }
           
    }

    private Command commandIncorrect(String message) {
        return new IncorrectCommand(String.format(message, HelpCommand.MESSAGE_USAGE));
    }
    
    /*
     * checks whether task has a deadline, is an event, or is floating,
     * and uses the appropriate constructors accordingly.
     *         String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
     * @return an AddCommand Object
     */    
    public Command prepareAdd(String task) {
        try {
            Task newTask = extractTask(task);
            return new AddCommand(newTask);
        } catch (MultiplePriorityException e) {
            return new IncorrectCommand(Messages.MESSAGE_MULTIPLE_PRIORITY);
        } catch (IllegalArgumentException e) {
            return new IncorrectCommand(Messages.MESSAGE_EMPTY_DESCRIPTION);
        }                      
    }     
    
    public Command prepareEdit(String input) {
        final Matcher edit_matcher = EDIT_FORMAT.matcher(input.trim());
        
        if (!edit_matcher.matches()) {
            return new IncorrectCommand(EditCommand.MESSAGE_USAGE);
        }
        
        // TODO let index start from 1
        int index = Integer.valueOf(edit_matcher.group("index")) - 1;     
        
        String task = edit_matcher.group("task");
      
        try {
            Task newTask = extractTask(task);
            return new EditCommand(index, newTask);
        } catch (IllegalArgumentException e) {
            return new IncorrectCommand(Messages.MESSAGE_EMPTY_DESCRIPTION);
        }  
    }
    
    public Command prepareDelete(String input) {
        
        int index;
        
        try {
            index = Integer.valueOf(input.trim()) - 1;
        } catch (NumberFormatException e) {
            return new IncorrectCommand(DeleteCommand.MESSAGE_USAGE);
        }

        return new DeleteCommand(index);
    }
    
    public Command prepareList(String input) {
        if (input.trim() == "") return new ListCommand();
        
        Triple<String, List<Date>, List<Boolean>> info = TimeParser.extractTime(input.trim());

        String priority = info.getLeft();
        List<Date> dates = info.getMiddle();
        
        if (ReferenceList.priorityDictionary.containsKey(priority)) {
            priority = ReferenceList.priorityDictionary.get(priority);
        }
        else {
            priority = "";
        }
        
        if (dates.size() > 0) {
            if (priority.equals("")) return new ListCommand(dates.get(0));
            else return new ListCommand(priority, dates.get(0));
        }
        
        else if (!priority.equals("")) return new ListCommand(priority);
        else return new ListCommand();
        
    }

    private Task extractTask(String raw) throws MultiplePriorityException, IllegalArgumentException {

        Pair<Integer, String> proc = getPriority(raw.trim());
        int priority = proc.getKey();
        String input = proc.getValue();    
        
        Triple<String, List<Date>, List<Boolean>> info = TimeParser.extractTime(input.trim());
        List<Date> dates = info.getMiddle();
        String description = info.getLeft();
        Boolean isInferred = info.getRight().get(0);
        Boolean isRecurring = info.getRight().get(1);
        
        if (description.trim().equals("")) {
            throw new IllegalArgumentException();
        }
        
        if (dates.isEmpty()) {
            return new Task(description);
        }
        else if (dates.size() == 1) { 
            return new Task(description,dates.get(0));
        }
        // compare dates if there are 2 dates
        else {
            if (dates.get(0).before(dates.get(1)))
                return new Task(description,dates.get(0),dates.get(1));
            else 
                return new Task(description,dates.get(1),dates.get(0));
        }
    }
    
    /*
     * 1 for high priority, 3 for low
     * 
     */
    private Pair<Integer,String> getPriority(String input) throws MultiplePriorityException{
        String [] levels = {"-h", "-m", "-l" };
        int priority = 0;
        int index = input.length();
        int find;
        
        for (int i = 0; i < levels.length; i++) {
            if ((find = argumentIndexInString(input,levels[i])) != -1) {
                priority = i+1;
                index = find;
            }
        }

        if (priority == 0) priority = 2;
        
        input = input.substring(0, index);
        logger.info(input);
        return Pair.of(priority,input);
    }
    
    private int argumentIndexInString(String str, String arg) {
        return (str.toLowerCase().lastIndexOf(arg.toLowerCase()));
    }
    
}
