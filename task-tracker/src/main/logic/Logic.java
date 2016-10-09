package main.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javafx.util.Pair;
import main.data.Task;
import main.logic.parser.MainParser;
import main.logic.parser.timeParser;

public class Logic {
    
    ArrayList<Task> tasksList;
    MainParser parser;
    
    public Logic() {
        tasksList = new ArrayList<Task>();
        parser = new MainParser();
    }
    
    /**
     * processes the input
     * 
     * @return text to be displayed
     */
    public String process(String input) {
        return parser.parse(input).execute().feedbackToUser;
//        timeParser.extractTime_test(input);
//        Pair<String,List<Date>> parsed= timeParser.extractTime(input);
//        System.out.println(parsed.getValue());
//        return parsed.getKey();
        
    }

}
