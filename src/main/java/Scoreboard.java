import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Scoreboard {
    private static final Path FILE = Paths.get("scores.txt");
    private static final int MAX_SCORES = 5;

    public static List<Integer> load() {
        List<Integer> scores = new ArrayList<>();

        if (!Files.exists(FILE)) {
            return scores;
        }
        try {
            List<String> lines = Files.readAllLines(FILE);

            for (int i = 0; i < lines.size(); i++) {
                String line = lines.get(i).trim();

                if (line.matches("\\d+") && line.length() < 10) {
                    scores.add(Integer.valueOf(line));
                }
            }
            scores.sort(Comparator.reverseOrder());

            while (scores.size() > MAX_SCORES) {
                scores.remove(scores.size() - 1);
            }
        } catch (IOException e) {
            System.out.println("Could not read " + FILE + ": " + e.getMessage()); //debug
        }
        return scores;
    }
    
    public static void addScore(int score) {
        if (score <= 0) {
            return;
        }
        
        List<Integer> scores = load();
        scores.add(score);
        scores.sort(Comparator.reverseOrder());

        while (scores.size() > MAX_SCORES) {
            scores.remove(scores.size() - 1);
        }
        List<String> lines = new ArrayList<>();

        for (int i = 0; i < scores.size(); i++) {
            lines.add(String.valueOf(scores.get(i)));
        }
        try {
            Files.write(FILE, lines);
        } catch (IOException e) {
            System.out.println("Could not save " + FILE + ": " + e.getMessage()); //debug
        }
    }
}