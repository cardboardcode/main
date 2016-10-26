package main.logic.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.Subscribe;

import main.commons.core.EventsCenter;
import main.commons.events.model.TaskTrackerChangedEvent;
import main.commons.events.model.UpdateListWithSuggestionsEvent;
import main.commons.events.ui.AutoCompleteEvent;
import main.commons.events.ui.KeyPressEvent;
import main.commons.events.ui.TabPressEvent;
import main.logic.autocomplete.SetTrie.TrieBuilder;
import main.logic.command.DeleteCommand;
import main.logic.command.DoneCommand;
import main.logic.command.EditCommand;
import main.logic.command.FindCommand;
import main.logic.command.ListCommand;
import main.logic.parser.ReferenceList;
import main.model.Model;
import main.model.ReadOnlyTaskTracker;
import main.model.task.ReadOnlyTask;

public class AutoComplete {
        
    private SetTrie commandList;
    private SetTrie listList;
    private List<SetTrie> taskList;
    private List<String> suggestions;
    private int start_index;
    private int end_index;
    private int tabCount = 0;
    EventsCenter eventsCenter;
    ReadOnlyTaskTracker tasktracker;
    
    public AutoComplete(Model model) {
        this.eventsCenter = EventsCenter.getInstance().registerHandler(this);
        this.suggestions = new ArrayList<String>();
        
        buildAllLists(model);
    }

    private void buildAllLists(Model model) {
        buildCommandList();
        buildListList();
        updateSuggestions("");
        taskList = new ArrayList<SetTrie>();
        updateTaskList(model.getTaskTracker());
    }
    
    private void buildCommandList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        build.add(ReferenceList.commandsDictionary.keySet());
        commandList = build.build();
    }
 
    private void buildListList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        build.add(ReferenceList.listSet).add(ReferenceList.doneSet);
        listList = build.build();
    }
    
    /*
     * updates the taskList by iterating all tasks and storing
     * them into an array of SetTrie.
     */
    private void updateTaskList(ReadOnlyTaskTracker data) {
        tasktracker = data;

        for (ReadOnlyTask task: data.getTaskList()) {
            SetTrie trie = SetTrie.builder().caseInsensitive()
                                  .add(Arrays.stream(getTokens(task.getMessage())).collect(Collectors.toSet()))
                                  .build();
            taskList.add(trie);
        }
    }
    
    /*
     * resets the taskList using the tasktracker's data
     */
    private void resetTaskList(){
        if (tasktracker == null) return;
        updateTaskList(tasktracker);
    }

    private String[] getTokens(String input) {
        return input.trim().split(" ");
    }
    
    public void updateSuggestions(String input) {
        String[] tokens = getTokens(input);
        end_index = input.length();
        
        // is command
        if (tokens.length == 1) {
            suggestions = commandList.getSuggestions(input);
            start_index = 0;
        }
        else {
            String commandInput = tokens[0];
            
            if (ReferenceList.commandsDictionary.containsKey(commandInput)) {
                String commandWord = ReferenceList.commandsDictionary.get(commandInput);
                start_index = commandInput.length() + 1;
                
                if (isFindEditDoneDelete(commandWord) && !isToNotDisturb(tokens, commandWord)) {
                    getTaskSuggestions(tokens, commandInput);
                }
                else if(commandWord.equals(ListCommand.COMMAND_WORD)) {
                    getListSuggestions(tokens);
                }
            }
        }
        resetTaskList();
    }
    
    /*
     * returns suggestions for list parameters
     */
    private void getListSuggestions(String[] tokens) {
        suggestions = listList.getSuggestions(tokens[tokens.length-1]);
    }

    /*
     * returns suggestions for tasks
     */
    private void getTaskSuggestions(String[] tokens, String commandInput) {
        int size = updateFilteredListWithSuggestions(ArrayUtils.subarray(tokens, 1, tokens.length));
        suggestions = getStringArrayFromIndex(size);
    }

    private boolean isToNotDisturb(String[] tokens, String commandWord) {
        return !commandWord.equals(FindCommand.COMMAND_WORD) && StringUtils.isNumeric(tokens[1]);
    }

    private boolean isFindEditDoneDelete(String commandWord) {
        return commandWord.equals(EditCommand.COMMAND_WORD) || commandWord.equals(DeleteCommand.COMMAND_WORD) || commandWord.equals(DoneCommand.COMMAND_WORD) || commandWord.equals(FindCommand.COMMAND_WORD) || commandWord.equals(DoneCommand.COMMAND_WORD);
    }
    
    /*
     * updates the filtered list in real time to show matching tasks
     * 
     * @returns the size of the updated filtered list
     */
    public int updateFilteredListWithSuggestions(String[] tokens) {
        taskList = taskList.stream().filter(trie -> containPrefixInTask(trie, tokens)).collect(Collectors.toList());
        List<ReadOnlyTask> matchedTasks = getListOfMatchedTasks();
        eventsCenter.post(new UpdateListWithSuggestionsEvent(matchedTasks));
        return matchedTasks.size();
    }
    
    private boolean containPrefixInTask(SetTrie task, String[] tokens) {

        for (String token : tokens) {
            if (task.matchPrefix(token)) {
                return true;
            }
        }
        return false;
    }
    
    private List<ReadOnlyTask> getListOfMatchedTasks() {
        List<ReadOnlyTask> matchedTasks = new ArrayList<ReadOnlyTask>();
        
        for (ReadOnlyTask task : tasktracker.getTaskList()) {
            for (SetTrie trie : taskList) {
                if (task.getMessage().equals(trie.toString())){
                    matchedTasks.add(task);
                    break;
                }
            }
        }
        return matchedTasks;
    }
    
    private List<String> getStringArrayFromIndex(int size) {
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < size; i++) {
            list.add(String.valueOf(i + 1));
        }
        return list;
    }
    
    /*
     * posts an autocomplete event to fill the commandBox
     */
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
        updateSuggestions(event.getInput());
        tabCount = 0;
    }
    
    @Subscribe
    private void handleTabPressEvent(TabPressEvent event) {
        fillInSuggestions();
        tabCount++;
    }
    
    @Subscribe
    private void handleTaskTrackerChangedEvent(TaskTrackerChangedEvent event) {
        updateTaskList(event.data);
    }

}
