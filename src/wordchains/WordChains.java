package wordchains;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import wordchains.exceptions.DifferentWordLengthsException;
import wordchains.exceptions.WordNotInDictionaryException;

/**
 *
 * @author Adam Parys
 */
public class WordChains {

    final static String TEST_FILENAME = "wordListTest.txt";
    final static String REAL_FILENAME = "wordList.txt";
    private ArrayList<String> dictionaryWords;
    HashMap<String, ArrayList<String>> directConnections;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WordChains wc = new WordChains(REAL_FILENAME);
        try {
            System.out.println(wc.getChainBetween("cat", "dog"));
            System.out.println(wc.getChainBetween("dog", "cat"));
            System.out.println(wc.getChainBetween("lead", "gold"));
            System.out.println(wc.getChainBetween("ruby", "code"));
            System.out.println(wc.getChainBetween("code", "ruby"));

        } catch (Exception ex) {
            Logger.getLogger(WordChains.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public WordChains(String filename) {
        try {
            dictionaryWords = new Dictionary(filename).getWords();
        } catch (IOException ex) {
            Logger.getLogger(WordChains.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ArrayList<String> getChainBetween(String start, String end) throws DifferentWordLengthsException, WordNotInDictionaryException {
        ArrayList<String> results = new ArrayList<>();
        if (start.length() != end.length()) {
            throw new DifferentWordLengthsException();
        }
        if (!dictionaryWords.contains(start)
                || !dictionaryWords.contains(end)) {
            throw new WordNotInDictionaryException();
        }

        if (WordUtil.areWordsDirectlyConnected(start, end)) {
            results.add(start);
            results.add(end);
            return results;
        }

        ArrayList<String> sameLengthWords = filterByLength(dictionaryWords, start.length());

        //the key of HashMap is a word and the value is a list of all words directly connected to the key word 
        findDirectlyConnectedWords(sameLengthWords);

        ArrayList<String> wordsToConnect = filterOutUnreachableWords();
        wordsToConnect.remove(start);

        //the key is length of chain and the value is a list of chains going out from start
        HashMap<Integer, ArrayList<Chain>> shortestChainsFromStart = new HashMap();

        shortestChainsFromStart.put(2, getDirectConnectionsFromStart(start, wordsToConnect));
        int count = 3;
        do {
            ArrayList<Chain> longestChainsFromStart = shortestChainsFromStart.get(count - 1);

            Chain possibleResult = checkIfChainsCanConnectToEnd(longestChainsFromStart, end);
            if (possibleResult != null) {
                return possibleResult.getWords();
            }

            ArrayList<Chain> newChains = new ArrayList<>();
            for (Iterator<String> it = wordsToConnect.iterator(); it.hasNext();) {
                String wordToConnect = it.next();
                if (wordToConnect.equals(end)) {
                    continue;
                }
                for (Chain chain : longestChainsFromStart) {
                    if (areConnected(chain, wordToConnect)) {
                        Chain newChain = new Chain(chain);
                        newChain.addWord(wordToConnect);
                        newChains.add(newChain);
                        it.remove();
                        break;
                    }
                }
            }
            shortestChainsFromStart.put(count, newChains);
            count++;
        } while (!wordsToConnect.isEmpty());
        return null;
    }

    private Chain checkIfChainsCanConnectToEnd(ArrayList<Chain> longestChainsFromStart, String end) {
        for (Chain chain : longestChainsFromStart) {
            if (areConnected(chain, end)) {
                Chain newChain = new Chain(chain);
                newChain.addWord(end);
                return newChain;
            }
        }
        return null;
    }

    private ArrayList<Chain> getDirectConnectionsFromStart(String start, ArrayList<String> wordsToConnect) {
        ArrayList<Chain> directConnectionsFromStart = directConnections.get(start).stream().map(word -> {
            Chain c = new Chain();
            c.addWord(start);
            c.addWord(word);
            wordsToConnect.remove(word);
            return c;
        }).collect(Collectors.toCollection(ArrayList::new));
        return directConnectionsFromStart;
    }

    private boolean areConnected(Chain chain, String word) {
        return directConnections.get(word).contains(chain.getLast());
    }

    private ArrayList<String> filterOutUnreachableWords() {
        ArrayList<String> wordsToConnect = new ArrayList<>();
        directConnections.forEach((k, v) -> {
            if (!v.isEmpty()) {
                wordsToConnect.add(k);
            }
        });
        return wordsToConnect;
    }

    private ArrayList<String> filterByLength(ArrayList<String> list, int length) {
        return list.stream()
                .filter(x -> x.length() == length)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void findDirectlyConnectedWords(ArrayList<String> dictionary) {
        HashMap<String, ArrayList<String>> result = new HashMap<>();
        for (int i = 0; i < dictionary.size(); i++) {
            String word1 = dictionary.get(i);
            for (int j = i; j < dictionary.size(); j++) {
                String word2 = dictionary.get(j);
                try {
                    if (i != j && WordUtil.areWordsDirectlyConnected(word1, word2)) {
                        if (result.get(word1) == null) {
                            result.put(word1,new ArrayList<>());
                        }
                        result.get(word1).add(word2);
                        
                        ArrayList<String> listForWord2 = result.get(word2);
                        if (result.get(word2) == null) {
                            result.put(word2,new ArrayList<>());
                        }
                         result.get(word2).add(word1);
                    }
                } catch (DifferentWordLengthsException ex) {
                    Logger.getLogger(WordChains.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

        directConnections = result;
    }

}
