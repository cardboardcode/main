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
import main.model.task.ReadOnlyTask;
import main.model.task.Task;
import main.model.task.UniqueTaskList.DuplicateTaskException;
import main.testutil.TestUtil;
import main.testutil.TypicalTestTasks;

public class AutoCompleteTest {
    AutoComplete autoComplete;
    TaskTracker taskTracker;
    Model model;
    
    @Before
    public void setup() {
        TypicalTestTasks typical = new TypicalTestTasks();
        taskTracker = typical.getTypicalTaskTracker();
        model = new ModelManager(taskTracker, new UserPrefs());
        autoComplete = new AutoComplete(model);
//        EventsCenter.clearSubscribers();

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
    public void updateSuggestions_list_paramsNoMatch() {
        autoComplete.updateSuggestions("list asdgsvfc");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_list_upperMatch() {
        autoComplete.updateSuggestions("list EV");
        assertEquals(Collections.singletonList("event "), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_matchFirstToken() {
        autoComplete.updateSuggestions("delete clea");
        assertEquals(Collections.singletonList("1"), autoComplete.getSuggestions());
        assertShownListEquals(model.getFilteredTaskList(), Arrays.asList(TypicalTestTasks.floating1));
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_matchMiddleToken() {
        autoComplete.updateSuggestions("done with");
        assertEquals(Collections.singletonList("1"), autoComplete.getSuggestions());
        assertShownListEquals(model.getFilteredTaskList(), Arrays.asList(TypicalTestTasks.event1));
    }
    
    @Test
    public void updateSuggestions_editDoneDelete_matchMultiple() {
        autoComplete.updateSuggestions("delete clea with");
        assertEquals(Arrays.asList("1", "2"), autoComplete.getSuggestions());
        assertShownListEquals(model.getFilteredTaskList(), Arrays.asList(TypicalTestTasks.floating1, TypicalTestTasks.event1));
    }
    
    @Test
    public void updateSuggestions_find_lowerCase() {
        autoComplete.updateSuggestions("find clea ");
        assertEquals(Arrays.asList("1"), autoComplete.getSuggestions());
        assertShownListEquals(model.getFilteredTaskList(), Arrays.asList(TypicalTestTasks.floating1));
    }
    
    @Test
    public void updateSuggestions_find_upperCase() {
        autoComplete.updateSuggestions("find CLEA ");
        assertEquals(Arrays.asList("1"), autoComplete.getSuggestions());
        assertShownListEquals(model.getFilteredTaskList(), Arrays.asList(TypicalTestTasks.floating1));
    }
    
    @Test
    public void updateSuggestions_sort_paramsNoMatch() {
        autoComplete.updateSuggestions("sort efad");
        assertEquals(new ArrayList<String>(), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_sort_lowerMatch() {
        autoComplete.updateSuggestions("sort da");
        assertEquals(Arrays.asList("date"), autoComplete.getSuggestions());
    }
    
    @Test
    public void updateSuggestions_sort_upperMatch() {
        autoComplete.updateSuggestions("sort DA");
        assertEquals(Arrays.asList("date"), autoComplete.getSuggestions());
    }    
    
    private List<String> setToSortedListMatchingInput(Set<String> set, String input) {
        return set.stream().filter(k -> k.substring(0,input.length()).equals(input)).sorted((k1, k2) -> k1.compareTo(k2)).collect(Collectors.toList());
    }
    
    private boolean assertShownListEquals(List<ReadOnlyTask> actual, List<ReadOnlyTask> expected) {
        return actual.equals(expected);
    }
    
}
