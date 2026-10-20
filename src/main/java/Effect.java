import java.util.function.Consumer;

public class Effect {
    private final String name;
    private final int seconds;
    private final Consumer<GameContext> onStart;
    private final Consumer<GameContext> onEnd;
    private boolean buff = false; //false = debuff
    private String imagePath;     //optional image for the popup (null = none)

    public Effect(String name, int seconds,
                  Consumer<GameContext> onStart, Consumer<GameContext> onEnd) {
        this.name = name;
        this.seconds = seconds;
        this.onStart = onStart;
        this.onEnd = onEnd;
    }

    public static Effect instant(String name, Consumer<GameContext> action) {
        return new Effect(name, 0, action, ctx -> { });
    }

    public Effect asBuff() {
        buff = true;
        return this;
    }

    public Effect asDebuff() {
        buff = false;
        return this;
    }

    public Effect withImage(String resourcePath) {
        this.imagePath = resourcePath;
        return this;
    }

    public void start(GameContext ctx) {
        onStart.accept(ctx);
    }

    public void end(GameContext ctx) {
        onEnd.accept(ctx);
    }

    public boolean isInstant() {
        return seconds == 0;
    }

    public String getImagePath() {
        return imagePath;
    }

    public boolean isBuff() {
        return buff;
    }

    public int getSeconds() {
        return seconds;
    }

    public long getDurationNanos() {
        return seconds * 1_000_000_000L;
    }

    public String getName() {
        return name;
    }
}