package wordchains;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
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

    final static String TEST_FILENAME = "wordList.txt";
    private ArrayList<String> dictionaryWords;
    HashMap<String, ArrayList<String>> directConnections;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        WordChains wc = new WordChains(TEST_FILENAME);
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

        if (WordUtil.charDifference(start, end) <= 1) {
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
            ArrayList<String> connectedWords = new ArrayList<>();
            ArrayList<Chain> longestChainsFromStart = shortestChainsFromStart.get(count - 1);
            ArrayList<Chain> newChains = new ArrayList<>();
            for (String wordToConnect : wordsToConnect) {
                for (Chain chain : longestChainsFromStart) {
                    if (areConnected(chain, wordToConnect)) {
                        Chain newChain = new Chain(chain);
                        newChain.addWord(wordToConnect);
                        if (wordToConnect.equals(end)) {
                            return newChain.getWords();
                        }
                        newChains.add(newChain);
                        connectedWords.add(wordToConnect);
                        break;
                    }
                }
            }
            wordsToConnect.removeAll(connectedWords);
            shortestChainsFromStart.put(count, newChains);
            count++;
        } while (!wordsToConnect.isEmpty());
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
            ArrayList<String> connections = new ArrayList<>();
            for (int j = 0; j < dictionary.size() - 1; j++) {
                String word2 = dictionary.get(j);
                try {
                    if (i != j && WordUtil.charDifference(word1, word2) == 1) {
                        connections.add(word2);
                    }
                } catch (DifferentWordLengthsException ex) {
                    Logger.getLogger(WordChains.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            result.put(word1, connections);
        }

        directConnections = result;
    }

}
