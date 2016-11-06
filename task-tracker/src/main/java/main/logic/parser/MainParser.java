//@@author A0144132W
package main.logic.parser;

import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.core.Messages;
import main.commons.events.ui.updateListStatisticsPictureEvent;
import main.commons.exceptions.IllegalValueException;
import main.commons.exceptions.MultiplePriorityException;

import java.util.Date;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

import main.model.task.PriorityType;
import main.model.task.Task;
import main.model.task.TaskType;
import main.logic.command.AddCommand;
import main.logic.command.ClearCommand;
import main.logic.command.Command;
import main.logic.command.DeleteCommand;
import main.logic.command.DoneCommand;
import main.logic.command.EditCommand;
import main.logic.command.ExitCommand;
import main.logic.command.FindCommand;
import main.logic.command.HelpCommand;
import main.logic.command.IncorrectCommand;
import main.logic.command.ListCommand;
import main.logic.command.RedoCommand;
import main.logic.command.SortCommand;
import main.logic.command.StorageCommand;
import main.logic.command.UndoCommand;

public class MainParser {
    
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<task>.*)");
    private static final Pattern EDIT_FORMAT = Pattern.compile("(?<index>\\d)(?<task>.*)");
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
            return commandIncorrect(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_UNKNOWN_COMMAND));
        }

        return getCommand(input, commandWord, task);
           
    }

    private Command getCommand(String input, String commandWord, String task) {
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
            case DoneCommand.COMMAND_WORD:
                return prepareDone(task);
            case FindCommand.COMMAND_WORD:
                return new FindCommand();
            case ClearCommand.COMMAND_WORD:
                return new ClearCommand();
            case UndoCommand.COMMAND_WORD:
                return prepareUndo();
            case RedoCommand.COMMAND_WORD:
                return prepareRedo();
            case StorageCommand.COMMAND_WORD:
                return prepareStorage(input);
            case SortCommand.COMMAND_WORD:
                return prepareSort(task);
            default: 
                return commandIncorrect(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_UNKNOWN_COMMAND));
        }
    }

    private Command commandIncorrect(String message) {
        return new IncorrectCommand(String.format(message, HelpCommand.MESSAGE_USAGE));
    }
    
    /*
     * checks whether task has a deadline, is an event, or is floating,
     * and uses the appropriate constructors accordingly.
     * 
     * @return an AddCommand Object
     */    
    public Command prepareAdd(String task) {
        try {
            Task newTask = extractTask(task);
            return new AddCommand(newTask);
        } catch (MultiplePriorityException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_MULTIPLE_PRIORITY));
        } catch (IllegalArgumentException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, Messages.MESSAGE_EMPTY_DESCRIPTION));
        }                      
    }     
    
    public Command prepareEdit(String input) {
        final Matcher edit_matcher = EDIT_FORMAT.matcher(input.trim());
        
        if (!edit_matcher.matches()) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }
        
        int index;
        
        try {
            index = extractValidIndex(edit_matcher.group("index"));
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(e.getMessage(), EditCommand.MESSAGE_USAGE));
        } 
                
        String task = edit_matcher.group("task");
      
        try {
            Task newTask = extractTask(task);
            return new EditCommand(index, newTask);
        } catch (IllegalArgumentException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, EditCommand.MESSAGE_USAGE));
        }  
    }
    
    public Command prepareDelete(String input) {
        int index;
        
        try {
            index = extractValidIndex(input);
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DeleteCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(e.getMessage(), DeleteCommand.MESSAGE_USAGE));
        } 
   
        return new DeleteCommand(index);
    }

    
    public Command prepareDone(String input) {
        int index;
        
        try {
            index = extractValidIndex(input);
        } catch (NumberFormatException e) {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, DoneCommand.MESSAGE_USAGE));
        } catch (IllegalValueException e) {
            return new IncorrectCommand(String.format(e.getMessage(), DoneCommand.MESSAGE_USAGE));
        } 
   
        return new DoneCommand(index);
    }

    private int extractValidIndex(String input) throws IllegalValueException, NumberFormatException{
        if (input.trim() == "")  {
            throw new IllegalValueException(Messages.MESSAGE_INVALID_TASK_DISPLAYED_INDEX); 
        }
        
        int index = Integer.valueOf(input.trim()) - 1;
        
        if (index < 0) {
            throw new IllegalValueException(Messages.MESSAGE_INVALID_INDEX); 
        }

        return index;
    }
    
  //@@author A0142686X
    public Command prepareUndo() {
        return new UndoCommand();
    }
    
    public Command prepareRedo() {
        return new RedoCommand();
    }
    
    public Command prepareStorage(String input) {
        if(input.trim() == "") {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_COMMAND_FORMAT, StorageCommand.MESSAGE_USAGE));
        }
        String path = extractPath(input.trim());
        return new StorageCommand(path);
    }
    
    public String extractPath(String input) {
        return input.substring(8);
    }
    
    //@@author A0144132W
    public Command prepareList(String input) {
        if (input.trim().equals("")) return new ListCommand();
        
        Triple<String, List<Date>, List<Boolean>> info = TimeParser.extractTime(input.trim());

        String left = info.getLeft();
        List<Date> dates = info.getMiddle();
        PriorityType priority = null;
        TaskType type = null;
        boolean isDone = false;
        boolean onlyOverdue = false;
     
        for (String param: left.trim().split(" ")) {
            if (ReferenceList.priorityDictionary.containsKey(param) && priority == null) {
                priority = ReferenceList.priorityDictionary.get(param);
            }
            else if (ReferenceList.typeDictionary.containsKey(param) && type == null) {
                type = ReferenceList.typeDictionary.get(param);
            }
            else if (ReferenceList.doneSet.contains(param)) {
                isDone = true;
            }
            else if (param.equals(ReferenceList.overdue)) {
                onlyOverdue = true;
            }
            else if (!param.trim().equals("")) {
                return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_PARAMETERS,"ListCommand", ListCommand.MESSAGE_USAGE));
            }           
        }  
        
        Date date;
        date = getDate(dates);

        indicateListParamsChanged(Triple.of(priority, date, type));
        return new ListCommand(Triple.of(priority, date, type), isDone, onlyOverdue);
    }

    private Date getDate(List<Date> dates) {
        Date date;
        if (dates.size() > 0) {
            date = dates.get(0);
        }
        else {
            date = null;
        }
        return date;
    }
    
    public Command prepareSort(String input) {
        input = input.trim().toLowerCase();
        
        if (input.equals("date") || input.equals("name")) {
            return new SortCommand(input);
        }
        else {
            return new IncorrectCommand(String.format(Messages.MESSAGE_INVALID_PARAMETERS, "sort", SortCommand.MESSAGE_USAGE));
        }
    }

    private Task extractTask(String raw) throws MultiplePriorityException, IllegalArgumentException {

        Pair<PriorityType, String> proc = getPriority(raw.trim());
        PriorityType priority = proc.getKey();
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
            return (new Task(description, priority)).setIsInferred(isInferred).setIsRecurring(isRecurring);
        }
        else if (dates.size() == 1) { 
            return new Task(description,dates.get(0), priority).setIsInferred(isInferred).setIsRecurring(isRecurring);
        }
        // compare dates if there are 2 dates
        else {
            if (dates.get(0).before(dates.get(1)))
                return new Task(description,dates.get(0),dates.get(1), priority).setIsInferred(isInferred).setIsRecurring(isRecurring);
            else 
                return new Task(description,dates.get(1),dates.get(0), priority).setIsInferred(isInferred).setIsRecurring(isRecurring);
        }
    }
    
    /*
     * uses PriorityType enum class
     * 
     * @return a pair consisting PriorityType and truncated message (without priority indicator)
     */
    private Pair<PriorityType,String> getPriority(String input) throws MultiplePriorityException{
        String [] levels = {"-h", "-m", "-l" };
        int priority = 0;
        int index = input.length();
        int find;
        
        for (int i = 0; i < levels.length; i++) {
            if ((find = argumentIndexInString(input,levels[i])) != -1) {
                if (priority != 0) {
                    throw new MultiplePriorityException();
                }
                priority = i+1;
                index = find;
            }
        }

        if (priority == 0) {
            priority = 2;
        }
        
        PriorityType priority_enum;
        
        if (priority == 1) {
            priority_enum = PriorityType.HIGH;
        }
        else if (priority == 2) {
            priority_enum = PriorityType.NORMAL;
        }
        else {
            priority_enum = PriorityType.LOW;
        }
        
        input = input.substring(0, index);
        
        return Pair.of(priority_enum,input);
    }
    
    private int argumentIndexInString(String str, String arg) {
        return (str.toLowerCase().lastIndexOf(arg.toLowerCase()));
    }
    
    /*
     * processes the list parameters and chooses one to be shown in the list statistics
     * by posting an event
     */
    private void indicateListParamsChanged(Triple<PriorityType, Date, TaskType> params) {
        String paramToShow = "";
        if (params.getLeft() != null) {
            paramToShow = params.getLeft().name();
        }
        else if (params.getMiddle() != null) {
            paramToShow = "date";
        }
        else if (params.getRight() != null) {
            paramToShow = params.getRight().name();
        }
        else {
            paramToShow = "list";
        }
            
        EventsCenter.getInstance().post(new updateListStatisticsPictureEvent(paramToShow));
    }
    
}
