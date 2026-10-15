//change this in regard to adding lambda expression so that every DEBUG
//seperate objective has its own trigger attached to it. DEBUG
//also use this for buffs and debuffs DEBUG

public class Objective {
    private final String description;
    private final int target;
    private final Type type;
    
    public enum Type {
        LINES, PIECES, SCORE
    }
    
    public Objective(String description, Type type, int target) {
        this.description = description;
        this.target = target;   
        this.type = type;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Type getType() {
        return type;
    }
    
    public int getTarget() {
        return target;
    }
}
