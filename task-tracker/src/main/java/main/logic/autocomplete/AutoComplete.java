package main.logic.autocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.scene.input.KeyCode;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.ui.AutoCompleteEvent;
import main.commons.events.ui.KeyPressEvent;
import main.commons.events.ui.TabPressEvent;
import main.logic.autocomplete.SetTrie.TrieBuilder;
import main.logic.parser.ReferenceList;
import main.model.Model;
import main.model.ReadOnlyTaskTracker;
import main.model.task.ReadOnlyTask;
import main.model.task.Task;

public class AutoComplete {
        
    private SetTrie commandList;
    private SetTrie taskList;
    private List<String> suggestions;
    private int start_index;
    private int end_index;
    private int tabCount = 0;
    private Model model;
    EventsCenter eventsCenter;
    
    public AutoComplete(Model model) {
        this.eventsCenter = EventsCenter.getInstance().registerHandler(this);
        this.model = model;
        
        suggestions = new ArrayList<String>();
        buildCommandList();
        updateSuggestions("");
        buildTaskList();
    }
    
    private void buildCommandList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        for (String cmd : ReferenceList.commandsDictionary.keySet()) {
            build.addKeyword(cmd);
        }
        commandList = build.build();
    }
    
    private void buildTaskList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        for (ReadOnlyTask task: model.getTaskTracker().getTaskList()) {
            build.addKeyword(task.getMessage());
        }
        taskList = build.build();
    }
    
    private void updateTaskList(ReadOnlyTaskTracker data) {
        TrieBuilder build = SetTrie.builder(this.taskList).caseInsensitive();
        for (ReadOnlyTask task: data.getTaskList()) {
            build.addKeyword(task.getMessage());
        }
        taskList = build.build();        
    }
    
    private String[] getTokens(String input) {
        return input.trim().split(" ");
    }
    
    public void updateSuggestions(String input) {
        String[] tokens = getTokens(input);
        
        // is command
        if (tokens.length == 1) {
            suggestions = commandList.getSuggestions(input);
            start_index = 0;
            end_index = input.length();
        }
        else {
            
        }

    }
    
    public void fillInSuggestions() {
        if (suggestions.size() == 0) return;
        
        String suggest = suggestions.get(tabCount % suggestions.size());

        eventsCenter.post(new AutoCompleteEvent(start_index, end_index, suggest));
        end_index = start_index + suggest.length();
        
    }
    
    public List<String> getSuggestions() {
        return suggestions;
    }
    
    @Subscribe
    private void handleKeyPressEvent(KeyPressEvent event) {
        System.out.println(suggestions);
        
        updateSuggestions(event.getInput());
        tabCount = 0;
    }
    
    @Subscribe
    private void handleTabPressEvent(TabPressEvent event) {
        tabCount++;
        
        fillInSuggestions();
    }
    
    @Subscribe
    private void handleTaskTrackerChangedEvent(TaskTrackerChangedEvent event) {
        updateTaskList(event.data);
    }

}
