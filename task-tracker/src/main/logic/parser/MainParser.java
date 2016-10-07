package main.logic.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import main.data.Task;
import main.logic.command.AddCommand;
import main.logic.command.HelpCommand;
import main.logic.command.ListCommand;
import org.apache.commons.cli.*;

public class Parser {
    
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<task>.*)");
    

    ArrayList<Task>list;
    
    public Parser(ArrayList<Task> list) {
       this.list = list;
    }
    
    public String parse(String input){
        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(input.trim());
        
        if (!matcher.matches()) {
            return new HelpCommand().execute(list);
        }
        String commandWord = matcher.group("commandWord");
        String task = matcher.group("task");
        
        switch (commandWord) {
            case AddCommand.COMMAND_WORD:
                return new AddCommand(new Task(task)).execute(list);
            case ListCommand.COMMAND_WORD:
                return new ListCommand().execute(list);
            case HelpCommand.COMMAND_WORD:
                return new HelpCommand().execute(list);
        }
        return new HelpCommand().execute(list);   
            
    }

}
