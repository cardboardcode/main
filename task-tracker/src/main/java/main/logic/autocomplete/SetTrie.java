package main.logic.autocomplete;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

// Referenced from http://sujitpal.blogspot.sg/2007/02/three-autocomplete-implementations.html
public class SetTrie {

    private TreeSet<String> wordList;
    private boolean caseInsensitive; 
    

    public SetTrie(TreeSet<String> wordList, boolean caseInsensitive) {
        this.wordList = wordList;
        this.caseInsensitive = caseInsensitive;
        if (caseInsensitive) this.wordList.stream().map(String::toLowerCase); 
    }
   
    public boolean matchPrefix(String prefix) {
        if (caseInsensitive) prefix = prefix.toLowerCase();

        Set<String> tailSet = wordList.tailSet(prefix);
        for (String tail : tailSet) {
            if (tail.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
    
    public List<String> getSuggestions(String prefix) {
        if (caseInsensitive) prefix = prefix.toLowerCase();

        List<String> possibleList = new ArrayList<String>();
        Set<String> tailSet = wordList.tailSet(prefix);
        for (String tail : tailSet) {
            if (tail.startsWith(prefix)) {
                possibleList.add(tail);
            } 
            else {
                break;
            }
        }
        return possibleList;
    }
    
    public static TrieBuilder builder() {
        return new TrieBuilder();
    }
    
    public static class TrieBuilder {
        
        private TreeSet<String> wordList;
        private boolean caseInsensitive = false;
        
        public TrieBuilder caseInsensitive() {
            this.caseInsensitive = true;
            return this;
        }
        
        public TrieBuilder addKeyword(String keyword) {
            wordList.add(keyword);
            return this;
        }
        
        public SetTrie build() {
            return new SetTrie(wordList, caseInsensitive);
        }        
    }
}
