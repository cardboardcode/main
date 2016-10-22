package main.model.filter;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import main.model.task.Task;

public class SortFilter {
    

    //Sort by comparing deadlines and end date of events
    Comparator<Task> byTime = (t1, t2) -> t1.compareTime(t2);

    //Sort lexicographically, case insensitive
    Comparator<Task> byName = (t1, t2) -> t1.getMessage().toLowerCase().compareTo(t2.getMessage().toLowerCase());

    
    private Comparator<Task> sortCriteria;
    private boolean isReversed = false;
    
    public SortFilter(SortCriteria criteria) {
        switch (criteria) {
            case TIME:
                sortCriteria = byTime.thenComparing(byName);
//                isReversed = false;
                break;
            case NAME:
                sortCriteria = byName.thenComparing(byTime); 
//                isReversed = true; // so appears in the order a-z
                break;
            default:
                sortCriteria = byTime.thenComparing(byName);
//                isReversed = false;
                break;
        }
    }
    
    public Comparator<Task> getComparator() {
        if (isReversed) return sortCriteria.reversed();
        else return sortCriteria;
    }
    
    public void setReverse(boolean reverse) {
        isReversed = reverse;
    }
}
