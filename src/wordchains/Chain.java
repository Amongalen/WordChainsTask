package wordchains;

import java.util.ArrayList;

/**
 *
 * @author Adam Parys
 */
public class Chain {

    private ArrayList<String> words;

    public Chain() {
        words = new ArrayList<>();
    }
    
    public Chain(Chain chain) {
        words = new ArrayList<>();
        for (String word : chain.words) {
            this.words.add(word);
        }
    }
    
    public ArrayList<String> getWords() {
        return words;
    }
    
    public void addWord(String word){
        words.add(word);
    }
    
    public String getFirst(){
        return words.get(0);
    }
    
    public String getLast(){
        return words.get(words.size()-1);
    }
    
}
