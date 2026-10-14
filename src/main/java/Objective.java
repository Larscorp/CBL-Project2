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
