package wordchains;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Scanner;

/**
 *
 * @author Adam Parys
 */
public class Dictionary {

    private ArrayList<String> words = new ArrayList<>();

    public Dictionary(String filename) throws IOException {
        readDictionary(filename);
    }

    public ArrayList<String> getWords() {
        return words;
    }

    private void readDictionary(String filename) throws IOException {
        Path path = Paths.get(filename);
        try (Scanner scanner = new Scanner(path)) {
            while (scanner.hasNextLine()) {
                words.add(scanner.nextLine().trim());
            }
        }
    }
}
