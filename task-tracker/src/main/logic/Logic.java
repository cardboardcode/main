package main.logic;

import java.util.ArrayList;
import main.data.Task;
import main.logic.parser.Parser;

public class Logic {
    
    ArrayList<Task> tasksList;
    Parser parser;
    
    public Logic() {
        tasksList = new ArrayList<Task>();
        parser = new Parser(tasksList);
    }
    
    /**
     * processes the input
     * 
     * @return text to be displayed
     */
    public String process(String input) {
        return parser.parse(input);
    }

}
