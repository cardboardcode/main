//@@author A0144132W
package main.logic.autocomplete;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import main.commons.core.EventsCenter;
import main.commons.events.ui.TabPressEvent;
import main.logic.parser.ReferenceList;
import main.model.Model;
import main.model.ModelManager;
import main.model.TaskTracker;
import main.model.UserPrefs;
import main.model.task.PriorityType;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.testutil.TestUtil;
import main.testutil.TypicalTestTasks;

public class AutoCompleteTest {
    AutoComplete autoComplete;
    TaskTracker taskTracker;
    
    @Before
    public void setup() {
        TypicalTestTasks typical = new TypicalTestTasks();
        taskTracker = typical.getTypicalTaskTracker();
        autoComplete = new AutoComplete(new ModelManager(taskTracker, new UserPrefs()));
        EventsCenter.clearSubscribers();

    }
    
    @Test
    public void updateSuggestions_commands_noMatch() {
        autoComplete.updateSuggestions("asdgsvfc");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_commands_allMatch() {
        autoComplete.updateSuggestions("");
        assertEquals(ReferenceList.CommandsSetWithRelevantSpaces.stream().sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList()), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_commands_matchdLowerCase() {
        String input = "d"; 
        autoComplete.updateSuggestions(input);
        assertEquals(setToSortedListMatchingInput(ReferenceList.CommandsSetWithRelevantSpaces, input), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_commands_matchdUpperCase() {
        String input = "D"; 
        autoComplete.updateSuggestions(input);
        assertEquals(setToSortedListMatchingInput(ReferenceList.CommandsSetWithRelevantSpaces, input.toLowerCase()), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_list_matchFirstParam() {
        String input = "list hig";
        autoComplete.updateSuggestions(input);
        assertEquals(Collections.singletonList("high "), autoComplete.getSuggestions());
        
    }
    
    @Test
    public void updateSuggestions_list_matchSecondParam() {
        String input = "list high flo";
        autoComplete.updateSuggestions(input);
        assertEquals(Collections.singletonList("floating "), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_list_params_noMatch() {
        autoComplete.updateSuggestions("list asdgsvfc");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_matchFirstToken() {
        autoComplete.updateSuggestions("delete clea");
        assertEquals(Collections.singletonList("1"), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_matchMiddleToken() {
        autoComplete.updateSuggestions("done with");
        assertEquals(Collections.singletonList("1"), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_matchMultiple() {
        autoComplete.updateSuggestions("delete clea with");
        EventsCenter.getInstance().post(new TabPressEvent());
        assertEquals(Arrays.asList("1", "2"), autoComplete.getSuggestions());
    }
    
    private List<String> setToSortedListMatchingInput(Set<String> set, String input) {
        return set.stream().filter(k -> k.substring(0,input.length()).equals(input)).sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList());
    }
    
}
