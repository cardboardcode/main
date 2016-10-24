package main.logic.autocomplete;


import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.common.eventbus.Subscribe;

import javafx.scene.input.KeyCode;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
import main.commons.events.ui.AutoCompleteEvent;
import main.commons.events.ui.IncorrectCommandAttemptedEvent;
import main.commons.events.ui.KeyPressEvent;
import main.commons.events.ui.TabPressEvent;
import main.logic.autocomplete.SetTrie.TrieBuilder;
import main.logic.parser.MainParser;
import main.logic.parser.ReferenceList;
import main.model.Model;

public class AutoComplete {
    
    private static final Logger logger = LogsCenter.getLogger(AutoComplete.class);
    
    private SetTrie commandList;
    private List<String> suggestions;
    private int start_index;
    private int end_index;
    private int tabCount = 0;
    private Model model;
    
    public AutoComplete(Model model) {
        buildCommandList();
        suggestions = new ArrayList<String>();
        this.model = model;
    }
    
    private void buildCommandList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        for (String cmd : ReferenceList.commandsDictionary.keySet()) {
            build.addKeyword(cmd);
        }
        commandList = build.build();
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
        

    }
    
    public void fillInSuggestions() {
        if (suggestions.size() == 0) return;
        
        String suggest = suggestions.get(tabCount % suggestions.size());
        EventsCenter.getInstance().post(new AutoCompleteEvent(start_index, end_index, suggest));
        
    }
    
    @Subscribe
    private void handleKeyPressEvent(KeyPressEvent event) {
        System.out.println(suggestions);
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "updating suggestions"));
        
        updateSuggestions(event.getInput());
        tabCount = 0;

    }
    
    @Subscribe
    private void handleTabPressEvent(TabPressEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event, "TAB pressed"));
        tabCount++;
        
        fillInSuggestions();
    }

}
