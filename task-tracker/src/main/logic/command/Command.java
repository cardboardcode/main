package main.logic.command;

import java.util.ArrayList;
import main.data.Task;

public abstract class Command {
    
    public String execute(ArrayList<Task> list) {
        return "";
    }

}
