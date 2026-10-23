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
    private boolean paused = false;
    private long pauseTime;

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
        long end = getCurrentTime() + effect.getDurationNanos();
        for (int i = 0; i < running.size(); i++) {
            if (running.get(i).effect.getName().equals(effect.getName())) {
                running.get(i).endTime = end;
                return;
            }
        } 
        effect.start(ctx);
        running.add(new Running(effect, end));
    }

    public void update() {
        if (paused) {
            return;
        }
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

    public void pause() {
        if (paused) {
            return;
        }
        paused = true;
        pauseTime = System.nanoTime();
    }

    public void resume() {
        if (!paused) {
            return;
        }
        long pausedDuration = System.nanoTime() - pauseTime;
        for (int i = 0; i < running.size(); i++) {
            Running r = running.get(i);
            r.endTime += pausedDuration;
        }
        paused = false;
    }

    private long getCurrentTime() {
        if (paused) {
            return pauseTime;
        } else {
            return System.nanoTime();
        }
    }
    
    public void clear() {
        for (int i = 0; i < running.size(); i++) {
            running.get(i).effect.end(ctx);
        }
        running.clear();
    }

    public List<Info> snapshot() {
        long now = getCurrentTime();

        return running.stream()
                .map(r -> new Info(
                        r.effect.getName(),
                        r.effect.isBuff(),
                        Math.max(0, (r.endTime - now)
                                / 1_000_000_000L + 1)))
                .toList();
    }
}