package wordchains;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import wordchains.exceptions.ConnectionNotFoundException;
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

    public ArrayList<String> getChainBetween(String start, String end) throws DifferentWordLengthsException, WordNotInDictionaryException, ConnectionNotFoundException {
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

        ArrayList<String> wordsToConnect = filterWordsByLength(dictionaryWords, start.length());
        wordsToConnect.remove(start);

        //the key is length of chain and the value is a list of chains going out from start
        HashMap<Integer, ArrayList<Chain>> chainsFromStart = new HashMap();

        chainsFromStart.put(2, getDirectConnectionsFromStart(start, wordsToConnect));
        int count = 3;
        do {
            ArrayList<Chain> longestChainsFromStart = chainsFromStart.get(count - 1);

            Chain possibleResult = connectChainToEndIfPossible(longestChainsFromStart, end);
            if (possibleResult != null) {
                return possibleResult.getWords();
            }

            ArrayList<Chain> newChains = connectWordsToLongestChains(wordsToConnect, longestChainsFromStart, end);
            chainsFromStart.put(count, newChains);
            count++;
        } while (!(wordsToConnect.isEmpty() || chainsFromStart.get(count - 1).isEmpty()));
        throw new ConnectionNotFoundException();
    }

    private ArrayList<Chain> connectWordsToLongestChains(ArrayList<String> wordsToConnect, ArrayList<Chain> longestChainsFromStart, String end) {
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
        return newChains;
    }

    private Chain connectChainToEndIfPossible(ArrayList<Chain> longestChainsFromStart, String end) {
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
        ArrayList<Chain> directConnectionsFromStart = new ArrayList<>();
        for (Iterator<String> it = wordsToConnect.iterator(); it.hasNext();) {
            try {
                String word = it.next();
                if (WordUtil.areWordsDirectlyConnected(start, word)) {
                    Chain c = new Chain();
                    c.addWord(start);
                    c.addWord(word);
                    it.remove();
                    directConnectionsFromStart.add(c);
                }
            } catch (DifferentWordLengthsException ex) {
                Logger.getLogger(WordChains.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return directConnectionsFromStart;
    }

    private boolean areConnected(Chain chain, String word) {
        try {
            return WordUtil.areWordsDirectlyConnected(chain.getLast(), word);
        } catch (DifferentWordLengthsException ex) {
            Logger.getLogger(WordChains.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    private ArrayList<String> filterWordsByLength(ArrayList<String> list, int length) {
        return list.stream()
                .filter(x -> x.length() == length)
                .collect(Collectors.toCollection(ArrayList::new));
    }
}
