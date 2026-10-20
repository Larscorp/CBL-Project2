import java.util.ArrayList;
import java.util.Collections;
import java.util.function.Predicate;

public class RandomizeObjectives {
    private ArrayList<String> objectives = new ArrayList<>();
        
    public RandomizeObjectives() {
        refill();
    }
    
    public void refill() {
        objectives.add("CLEAR4");
        objectives.add("CLEAR5");
        objectives.add("CLEAR6");
        objectives.add("PLACE15");
        objectives.add("PLACE20");
        objectives.add("HARD9");
        //objectives.add("HOLD4"); -> need to add
        objectives.add("NOHOLD8");
        objectives.add("DOUBLE"); 
        objectives.add("2DOUBLES");
        objectives.add("TRIPLE");
        objectives.add("TETRIS");
        //objectives.add("CLEAR3");
        //objectives.add("CLEAR3_HARD5"); -> need to implement
        //objectives.add("CLEAR3_PLACE10"); -> need to implement
        //objectives.add("DOUBLE_HARD5"); -> need to implement
                
        Collections.shuffle(objectives);
    }
    
    public Objective getNextObjective() {
        if (objectives.isEmpty()) {
            refill();
        }
        String next = objectives.remove(0);
        
        Predicate<LockEvent> noHold = e -> !e.usedHold();
        
                return switch (next) {
            case "CLEAR4" -> new Objective("Clear 4 lines", 4,
                    Rules.count(LockEvent::linesCleared));
              
            case "CLEAR5" -> new Objective("Clear 5 lines", 5,
                    Rules.count(LockEvent::linesCleared)); 
                
            case "CLEAR6" -> new Objective("Clear 6 lines", 6,
                    Rules.count(LockEvent::linesCleared));
                
            case "PLACE15" -> new Objective("Place 15 pieces", 15,
                    Rules.countIf(e -> true));
            
            case "PLACE20" -> new Objective("Place 20 pieces", 20,
                    Rules.countIf(e -> true));
            
            case "HARD9" -> new Objective("Hard drop 9 pieces", 9,
                    Rules.countIf(LockEvent::hardDropped));
            
            //case "HOLD4" -> new Objective("Place 4 pieces in hold", 4,
                    //Rules.streakOf(Hold));    
                
            case "NOHOLD8" -> new Objective("Place 8 pieces in a row without hold", 8,
                    Rules.streakOf(noHold));    
            
            case "DOUBLE" -> new Objective("Clear a double", 1,
                    Rules.countIf(e -> e.linesCleared() >= 2));    
                
            case "2DOUBLES" -> new Objective("Clear 2 doubles", 2,
                    Rules.countIf(e -> e.linesCleared() >= 2));
            
            case "TRIPLE" -> new Objective("Clear a triple", 1,
                    Rules.countIf(e -> e.linesCleared() >= 3));
                
            case "TETRIS" -> new Objective("Clear a Tetris", 1,
                    Rules.countIf(e -> e.linesCleared() == 4));
                    
            default -> null;
        };
    }
}        

