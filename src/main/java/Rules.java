import java.util.function.Predicate;
import java.util.function.ToIntFunction;

public final class Rules {

    private Rules() { }

    public static Rule count(ToIntFunction<LockEvent> value) {
        return (progress, e) -> progress + value.applyAsInt(e);
    }

    public static Rule countIf(Predicate<LockEvent> condition) {
        return (progress, e) -> {
            if (condition.test(e)) {
                return progress + 1;
            } else {
                return progress;
            }
        };
    }

    public static Rule streakOf(Predicate<LockEvent> condition) {
        return (progress, e) -> {
            if (condition.test(e)) {
                return progress + 1;
            } else {
                return 0;
            }
        };
    }
}