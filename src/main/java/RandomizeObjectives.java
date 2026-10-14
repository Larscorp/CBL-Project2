import java.util.ArrayList;
import java.util.Collections;

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
        
        Collections.shuffle(objectives);
    }
    
    public Objective getNextObjective() {
        if (objectives.isEmpty()) {
            refill();
        }
        String next = objectives.remove(0);
        
        return switch (next) {
            case "CLEAR1" -> new Objective("Clear 1 line", Objective.Type.LINES, 1);
            case "CLEAR2" -> new Objective("Clear 2 lines", Objective.Type.LINES, 2);
            case "PLACE5" -> new Objective("Place 5 pieces", Objective.Type.PIECES, 5);
            case "SCORE300" -> new Objective("Score 300 points", Objective.Type.SCORE, 300);
            default -> null;
        };
    }
}        

