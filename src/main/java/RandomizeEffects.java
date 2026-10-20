import java.util.ArrayList;
import java.util.Collections;

public class RandomizeEffects {
    private ArrayList<String> buffs = new ArrayList<>();
    private ArrayList<String> debuffs = new ArrayList<>();
    private static final int HOLES_TO_FILL = 6;
    
    public RandomizeEffects() {
        refillBuffs();
        refillDebuffs();
    }

    private void refillBuffs() {
        buffs.add("SLOWFALL");
        buffs.add("SCOREBOOST");
        buffs.add("CLEARROW"); //maybe remove
        buffs.add("FILLHOLE");
        //still need to implement
        buffs.add("SLOWGHOSTS");
        buffs.add("DAMAGEGHOST"); //sets ghost back a little
        buffs.add("SHIELD");
        buffs.add("EXTRALIFE");
        buffs.add("SCOREBONUS"); //gives ... score (* level)
        Collections.shuffle(buffs);
    }

    private void refillDebuffs() {
        debuffs.add("FASTFALL");
        debuffs.add("NOHOLD");
        debuffs.add("GHOSTOFF");
        //still need to implement
        debuffs.add("BLINDNEXT"); //hide next piece
        debuffs.add("FASTGHOSTS");
        debuffs.add("SCORELOSS");
        debuffs.add("ROTATELOCK");
        debuffs.add("JAMMED"); //disable hard drop
        debuffs.add("EXTRAGHOST");
        debuffs.add("GARBAGE"); //add garbage row
        Collections.shuffle(debuffs);
    }

    private Effect fallDelay(String name, int seconds, double factor) {
        return new Effect(name, seconds,
                ctx -> ctx.movement().multiplyFallDelay(factor),
                ctx -> ctx.movement().multiplyFallDelay(1 / factor));
    }

    private Effect scoreMultiplier(String name, int seconds, double factor) {
        return new Effect(name, seconds,
                ctx -> ctx.mechanics().multiplyScore(factor),
                ctx -> ctx.mechanics().multiplyScore(1 / factor));
    }

    public Effect getNextBuff() {
        if (buffs.isEmpty()) {
            refillBuffs();
        }
        String next = buffs.remove(0);

        Effect effect = switch (next) {
            case "SLOWFALL" -> fallDelay("Slow fall", 10, 1.5);
            case "SCOREBOOST" -> scoreMultiplier("Double score", 15, 2.0);
            case "CLEARROW" -> Effect.instant("Clear bottom row",
                    ctx -> ctx.board().deleteRows(ctx.board().getBoardLength() - 1));
            case "FILLHOLE" -> Effect.instant("Fill " + HOLES_TO_FILL + " holes", ctx -> {
                for (int i = 0; i < HOLES_TO_FILL; i++) {
                    ctx.board().fillBottomHole(8);  
                }
            }).withImage("/newpackage/HolesFilled.png");
            default -> null;
        };
        return effect.asBuff();
    }

    public Effect getNextDebuff() {
        if (debuffs.isEmpty()) {
            refillDebuffs();
        }
        String next = debuffs.remove(0);

        Effect effect = switch (next) {
            case "FASTFALL" -> fallDelay("Fast fall", 10, 0.5);
            case "NOHOLD" -> new Effect("No hold", 30,
                    ctx -> ctx.movement().blockHold(),
                    ctx -> ctx.movement().unblockHold());
            case "GHOSTOFF" -> new Effect("Ghost off", 15,
                    ctx -> ctx.panel().setGhostVisible(false),
                    ctx -> ctx.panel().setGhostVisible(true));
            default -> null;
        };
        return effect.asDebuff();
    }
}