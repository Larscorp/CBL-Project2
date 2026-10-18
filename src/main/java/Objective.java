public class Objective {
    private final String description;
    private final int target;
    private final Rule rule;
    private int progress;
    
    public Objective(String description, int target, Rule rule) {
        this.description = description;
        this.target = target;   
        this.rule = rule;
    }
    
    public void onLock(LockEvent event) {
        progress = rule.next(progress, event);
    }
    
    public String getDescription() {
        return description;
    }
    
    public int getProgress() {
        return progress;
    }
    
    public int getTarget() {
        return target;
    }
    
    public boolean isComplete() {
        return progress >= target;
    }
    
}
