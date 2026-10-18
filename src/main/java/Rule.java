@FunctionalInterface
public interface Rule {
    int next(int progress, LockEvent event);
}