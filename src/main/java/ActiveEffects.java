import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;

public class ActiveEffects {

    public record Info(String name, boolean buff, long secondsLeft) { }

    private static class Running {
        final Effect effect;
        long endTime;

        Running(Effect effect, long endTime) {
            this.effect = effect;
            this.endTime = endTime;
        }
    }

    private final GameContext ctx;
    private final ArrayList<Running> running = new ArrayList<>();
    private Consumer<Effect> onApply = e -> { }; 

    public ActiveEffects(GameContext ctx) {
        this.ctx = ctx;
    }

    public void setOnApply(Consumer<Effect> callback) {
        this.onApply = callback;
    }

    public void apply(Effect effect) {
        onApply.accept(effect);

        if (effect.isInstant()) {
            effect.start(ctx);
            return;
        }
        long end = System.nanoTime() + effect.getDurationNanos();

        for (Running r : running) {
            if (r.effect.getName().equals(effect.getName())) {
                r.endTime = end;
                return;
            }
        }
        effect.start(ctx);
        running.add(new Running(effect, end));
    }

    public void update() {
        long now = System.nanoTime();
        Iterator<Running> it = running.iterator();
        while (it.hasNext()) {
            Running r = it.next();
            if (now - r.endTime >= 0) {
                r.effect.end(ctx);
                it.remove();
            }
        }
    }

    public void clear() {
        for (Running r : running) {
            r.effect.end(ctx);
        }
        running.clear();
    }

    public List<Info> snapshot() {
        long now = System.nanoTime();
        return running.stream()
                .map(r -> new Info(r.effect.getName(), r.effect.isBuff(),
                        Math.max(0, (r.endTime - now) / 1_000_000_000L + 1)))
                .toList();
    }
}