//@@author A0144132W
package main.commons.events.model;

import java.util.List;

import main.commons.events.BaseEvent;
import main.model.task.ReadOnlyTask;

public class UpdateListWithSuggestionsEvent extends BaseEvent {
    private List<ReadOnlyTask> suggestions;
    
    public UpdateListWithSuggestionsEvent(List<ReadOnlyTask> suggestions) {
        this.suggestions = suggestions;
    }
    
    public List<ReadOnlyTask> getSuggestions() {
        return suggestions;
    }

    @Override
    public String toString() {
        return "" + suggestions.size() + " matching tasks found";
    }

}
