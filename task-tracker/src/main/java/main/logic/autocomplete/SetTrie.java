package main.logic.autocomplete;

import java.util.ArrayList;
import java.util.Collection;
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
    
    @Override
    public String toString(){
        StringBuilder stringBuilder = new StringBuilder();
        
        for (String word : wordList) {
            stringBuilder.append(word).append(" ");
        }
        return stringBuilder.toString().trim();
    }
    
    public static TrieBuilder builder() {
        return new TrieBuilder();
    }
    
    public static TrieBuilder builder(SetTrie trie) {
        return new TrieBuilder(trie);
    }
    
    public static class TrieBuilder {
        
        private TreeSet<String> wordList;
        private boolean caseInsensitive = false;
        
        private TrieBuilder() {
            wordList = new TreeSet<String>();
        }
        
        private TrieBuilder(SetTrie trie) {
            trie.wordList = new TreeSet<String>();
        }
        
        
        public TrieBuilder caseInsensitive() {
            this.caseInsensitive = true;
            return this;
        }
        
        public TrieBuilder add(Collection<String> words) {
            wordList.addAll(words);
            return this;
        }
        
        public SetTrie build() {
            return new SetTrie(wordList, caseInsensitive);
        }        
    }
}
