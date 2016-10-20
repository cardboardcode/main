package main.logic.parser;

import java.util.Map;
import com.google.common.collect.*;
import main.logic.command.*;
import main.model.task.PriorityType;

public class ReferenceList {
    
    public static final Map<String,String> commandsDictionary = ImmutableMap.<String, String>builder()
            .put("add", AddCommand.COMMAND_WORD)
            .put("create",AddCommand.COMMAND_WORD)
            .put("edit","edit")
            .put("change", "edit")
            .put("delete","delete")                        
            .put("del","delete")
            .put("remove","delete")
            .put("rm","delete")
            .put("rem","delete")
            .put("trash","delete")
            .put("done","finish")
            .put("finish","finish")
            .put("finished","finish")
            .put("complete","finish")               
            .put("completed","finish")
            .put("mark","finish")
            .put("undo","undo")
            .put("redo","redo")
            .put("exit","exit")
            .put("close","exit")
            .put("find","find")
            .put("search","find")
            .put("list", "list")
            .put("show", "list")
            .put("help", "help")
            .put("T.T", "help")
            .put("clear", "clear")
            .build(); 
    
    public static final Map<String,PriorityType> priorityDictionary = ImmutableMap.<String, PriorityType>builder()
            .put("high", PriorityType.HIGH)
            .put("impt", PriorityType.HIGH)
            .put("important", PriorityType.HIGH)
            .put("medium", PriorityType.NORMAL)
            .put("med", PriorityType.NORMAL)
            .put("normal", PriorityType.NORMAL)
            .put("low", PriorityType.LOW)
            .build();
    
    public static final Map<String,String> typeDictionary = ImmutableMap.<String, String>builder()
            .put("event", "event")
            .put("events", "event")
            .put("deadline", "deadline")
            .put("deadlines", "deadline")
            .put("floating", "floating")
            .put("floatings", "floating")
            .build();
            
    public ReferenceList() {}

}
