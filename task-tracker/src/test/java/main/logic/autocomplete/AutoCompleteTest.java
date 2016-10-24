package main.logic.autocomplete;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import main.logic.parser.ReferenceList;

public class AutoCompleteTest {
    AutoComplete autoComplete;
    
    @Before
    public void setup() {
        autoComplete = new AutoComplete();
    }
    
    @Test
    public void updateSuggestions_no_match() {
        autoComplete.updateSuggestions("asdgsvfc");
        assertEquals(autoComplete.getSuggestions(), new ArrayList<String>());
    }
    
    @Test
    public void updateSuggestions_all_match() {
        autoComplete.updateSuggestions("");
        
        // TODO maybe convert to list and compare
        assertEquals(autoComplete.getSuggestions(), ReferenceList.commandsDictionary.keySet().stream().sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList()));
    }
    
    @Test
    public void updateSuggestions_match_d() {
        String input = "d"; 
        autoComplete.updateSuggestions(input);
        
        assertEquals(autoComplete.getSuggestions(), ReferenceList.commandsDictionary.keySet().stream().filter(k -> k.substring(0,input.length()).equals(input)).sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList()));
    }

}
