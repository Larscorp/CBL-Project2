import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class RandomizeObjectives {
    private ArrayList<String> objectives = new ArrayList<>();
        
    public RandomizeObjectives() {
        refill();
    }
    
    public void refill() {
        objectives.add("CLEAR1");
        objectives.add("CLEAR2");
        objectives.add("PLACE5");
        objectives.add("SCORE300");
        objectives.add("HARD3");
        objectives.add("NOHOLD3");
        objectives.add("DOUBLE");
        objectives.add("TRIPLE");
        objectives.add("TETRIS");
        
        Collections.shuffle(objectives);
    }
    
    public Objective getNextObjective() {
        if (objectives.isEmpty()) {
            refill();
        }
        String next = objectives.remove(0);
        
        Predicate<LockEvent> noHold = e -> !e.usedHold();
        
                return switch (next) {
            case "CLEAR1" -> new Objective("Clear 1 line", 1,
                    Rules.count(LockEvent::linesCleared));
                
            case "CLEAR2" -> new Objective("Clear 2 lines", 2,
                    Rules.count(LockEvent::linesCleared));
                
            case "PLACE5" -> new Objective("Place 5 pieces", 5,
                    Rules.countIf(e -> true));
                
            case "SCORE300" -> new Objective("Score 300 points", 300,
                    Rules.count(LockEvent::pointsGained));
                
            case "HARD3" -> new Objective("Hard drop 3 pieces", 3,
                    Rules.countIf(LockEvent::hardDropped));
                
            case "NOHOLD3" -> new Objective("Place 3 pieces in a row without hold", 3,
                    Rules.streakOf(noHold));
                
            case "DOUBLE" -> new Objective("Clear a double", 1,
                    Rules.countIf(e -> e.linesCleared() >= 2));
            
            case "TRIPLE" -> new Objective("Clear a triple", 1,
                    Rules.countIf(e -> e.linesCleared() >= 3));
                
            case "TETRIS" -> new Objective("Clear a Tetris", 1,
                    Rules.countIf(e -> e.linesCleared() == 4));
            default -> null;
        };
    }
}        

