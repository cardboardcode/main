//@@author A0144132W
package main.logic.autocomplete;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.eventbus.Subscribe;

import org.apache.commons.lang3.tuple.Pair;
import main.commons.core.EventsCenter;
import main.commons.core.LogsCenter;
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
import main.logic.command.SortCommand;
import main.logic.parser.ReferenceList;
import main.model.Model;
import main.model.task.ReadOnlyTask;

public class AutoComplete {
        
    private static final Logger logger = LogsCenter.getLogger(AutoComplete.class);

    private SetTrie commandList;
    private SetTrie listList;
    private SetTrie sortList;
    private List<Pair<ReadOnlyTask,SetTrie>> taskList;
    private List<String> suggestions;
    private int start_index;
    private int end_index;
    private int tabCount = 0;
    EventsCenter eventsCenter;
    Model model;
    
    // to return to original list (when showing matching tasks in real time)
    private boolean save = false;
    private boolean revert = false;
    
    public AutoComplete(Model model) {
        this.eventsCenter = EventsCenter.getInstance().registerHandler(this);
        this.model = model;        
        buildAllLists(model);
        initSuggestions();
    }

    /*
     * fills the suggestions list with all commands at start up
     */
    private void initSuggestions() {
        suggestions = commandList.getSuggestions("");
    }
    
    private void buildAllLists(Model model) {
        buildCommandList();
        buildListList();
        buildSortList();
        taskList = new ArrayList<Pair<ReadOnlyTask,SetTrie>>();
        updateTaskList();
    }
    
    private void buildCommandList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        build.add(ReferenceList.CommandsSetWithRelevantSpaces);
        commandList = build.build();
    }
 
    private void buildListList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        build.add(ReferenceList.listSet);
        listList = build.build();
    }
    
    private void buildSortList() {
        TrieBuilder build = SetTrie.builder().caseInsensitive();
        build.add(ReferenceList.sortSet);
        sortList = build.build();
    }
    
    /*
     * updates the taskList by iterating all tasks and storing
     * them into an array of SetTrie.
     */
    private void updateTaskList() {

        for (ReadOnlyTask task: model.getTaskTracker().getTaskList()) {
            SetTrie trie = SetTrie.builder().caseInsensitive()
                                  .add(Arrays.stream(getTokens(task.getMessage())).collect(Collectors.toSet()))
                                  .build();

            taskList.add(Pair.of(task,trie));
        }
    }
    
    private String[] getTokens(String input) {
        return input.trim().split(" ");
    }
    
    //TODO slap this
    public void updateSuggestions(String input) {
        String[] tokens = getTokens(input);
        end_index = input.length();
        
        // is command
        if (tokens.length == 1) {
            suggestions = commandList.getSuggestions(input);
            start_index = 0;
            
            revertIfNeeded();
            save = true;
        }
        else {
            String commandInput = tokens[0];
            
            if (ReferenceList.commandsDictionary.containsKey(commandInput)) {
                String commandWord = ReferenceList.commandsDictionary.get(commandInput);
                
                if (needTaskSuggestions(tokens, commandWord)) {
                    start_index = commandInput.length() + 1;
                    saveIfNeeded();
                    getTaskSuggestions(tokens, commandInput);
                }
                else if (commandWord.equals(ListCommand.COMMAND_WORD)) {
                    start_index = getListSuggestions(tokens);
                }
                else if (commandWord.equals(SortCommand.COMMAND_WORD) && tokens.length == 2) {
                    getSortSuggestions(tokens[1]);
                    start_index = commandInput.length() + 1;
                }
            }
            else {
                suggestions = new ArrayList<String>();
            }
        }
        updateTaskList();
    }

    private void saveIfNeeded() {
        if (save) {
            model.saveFilter();
            save = false;
            revert = true;
        }
    }

    private void revertIfNeeded() {
        if (revert) {
            model.revertFilter();
            revert = false;
        }
    }
    
    private void getSortSuggestions(String token) {
        suggestions = sortList.getSuggestions(token);
    }
    
    /*
     * gets suggestions for last list parameter
     * 
     * @returns the start index of the command input to be replaced
     */
    private int getListSuggestions(String[] tokens) {
        suggestions = listList.getSuggestions(tokens[tokens.length-1]);
       
        int index = 0;
        for (int i = 0; i < tokens.length - 1; i++) {
            index += tokens[i].length() + 1;
        }
        
        return index;
    }

    /*
     * returns suggestions for tasks
     */
    private void getTaskSuggestions(String[] tokens, String commandInput) {
        int size = updateFilteredListWithSuggestions(ArrayUtils.subarray(tokens, 1, tokens.length));
        suggestions = getStringArrayFromIndex(size);
    }
    
    private boolean needTaskSuggestions(String[] tokens, String commandWord) {
        return isFindEditDoneDelete(commandWord) && !dontInterrupt(tokens, commandWord); 
    }

    /*
     * makes sure suggestions are not given when the user doesn't want it
     */
    private boolean dontInterrupt(String[] tokens, String commandWord) {
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
        taskList = taskList.stream().filter(pair -> containPrefixInTask(pair.getValue(), tokens)).collect(Collectors.toList());
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
        
        for (ReadOnlyTask task : model.getTaskTracker().getTaskList()) {
            for (Pair<ReadOnlyTask,SetTrie> pair : taskList) {
                if (task.equals(pair.getKey())){
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
    
    /*
     * @returns suggestions
     */
    public List<String> getSuggestions() {
        return suggestions;
    }
    
    /*
     * updates suggestions when key press is detected
     */
    @Subscribe
    private void handleKeyPressEvent(KeyPressEvent event) {
        updateSuggestions(event.getInput());
        tabCount = 0;
    }
    
    /*
     * toggles the suggestions to fill in commandBox
     */
    @Subscribe
    private void handleTabPressEvent(TabPressEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        fillInSuggestions();
        tabCount++;
    }
    
    /*
     * updates taskList when tasks are changed
     */
    @Subscribe
    private void handleTaskTrackerChangedEvent(TaskTrackerChangedEvent event) {
        updateTaskList();
    }

}
