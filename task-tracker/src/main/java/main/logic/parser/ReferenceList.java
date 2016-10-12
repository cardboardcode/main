package main.logic.parser;

import java.util.Map;
import com.google.common.collect.*;
import main.logic.command.*;

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
            .build(); 

    public ReferenceList() {}

}
