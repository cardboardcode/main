package main.logic.realtime;


import java.util.ArrayList;
import java.util.List;

import main.logic.parser.ReferenceList;
import main.logic.realtime.SetTrie.TrieBuilder;

public class AutoComplete {
    
    private SetTrie commandList;
    private List<String> suggestions;
    
    public AutoComplete() {
        buildCommandList();
        suggestions = new ArrayList<String>();
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
        }
    }

}
