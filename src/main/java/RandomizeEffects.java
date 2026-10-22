import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RandomizeEffects {
    private ArrayList<String> buffs = new ArrayList<>();
    private ArrayList<String> debuffs = new ArrayList<>();
    private static final int HOLES_TO_FILL = 6;
    private static final int SCORE_BONUS = 500;
    private static final int SCORE_LOSS = 300;
    private static final int GARBAGE_COLOR = 8;
    private int min = 1;
    private int max = 10;
    
    public RandomizeEffects() {
        refillBuffs();
        refillDebuffs();
    }

    private void refillBuffs() {
        buffs.add("SLOWFALL"); //pieces fall ... slower
        buffs.add("SCOREBOOST"); //boosts score ... x
        buffs.add("CLEARROW"); //clears bottom row
        buffs.add("FILLHOLE"); //fills ... holes
        buffs.add("SLOWGHOSTS"); //slows ghosts down for ... seconds
        buffs.add("SHIELD");  //gives shield agianst the next hit for ... seconds
        buffs.add("EXTRALIFE"); //+1 life
        buffs.add("SCOREBONUS"); //gives ... score * level
        Collections.shuffle(buffs);
    }

    private void refillDebuffs() {
        debuffs.add("FASTFALL"); //pieces fall ... faster
        debuffs.add("NOHOLD"); //unable to use hold for ... seconds
        debuffs.add("GHOSTOFF"); //turns the ghost piece off for ... seconds
        debuffs.add("BLINDNEXT"); //blinds the upcoming queue temporarily
        debuffs.add("FASTGHOSTS"); //ghosts move faster for ... seconds
        debuffs.add("SCORELOSS"); //removes ... score * level
        debuffs.add("ROTATELOCK"); //cant rotate for ... seconds
        debuffs.add("JAMMED"); //disables hard drop temporarily
        debuffs.add("EXTRAGHOST"); //adds ghost
        debuffs.add("GARBAGE"); //adds a garbage row
        debuffs.add("OSTAIR"); //adds either BigO or stair 
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

    // factor is a SPEED multiplier: 0.5 = half speed, 2.0 = double speed
    private Effect ghostSpeed(String name, int seconds, double factor) {
        return new Effect(name, seconds,
                ctx -> ctx.ui().multiplyGhostSpeed(factor),
                ctx -> ctx.ui().multiplyGhostSpeed(1 / factor));
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
                    ctx.board().fillHoles(8);
                }});
            case "SLOWGHOSTS" -> ghostSpeed("Slower ghosts", 30, 0.5);
            case "SHIELD" -> new Effect("Shielded", 60,
                    ctx -> ctx.ui().addShield(),
                    ctx -> ctx.ui().removeShield());
            case "EXTRALIFE" -> Effect.instant("Extra life",
                    ctx -> ctx.ui().addLife(1));
            case "SCOREBONUS" -> {int points = SCORE_BONUS * CBLProject.getBoard().getLevel();
                yield Effect.instant("You gain " + points + " points!",
                        ctx -> ctx.mechanics().addScore(points));
            }
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
            case "NOHOLD" -> new Effect("No hold", 15,
                    ctx -> ctx.movement().blockHold(),
                    ctx -> ctx.movement().unblockHold());
            case "GHOSTOFF" -> new Effect("Ghost off", 15,
                    ctx -> ctx.panel().setGhostVisible(false),
                    ctx -> ctx.panel().setGhostVisible(true));
            case "BLINDNEXT" -> new Effect("Blind the queue", 15,
                    ctx -> ctx.ui().setQueueHidden(true),
                    ctx -> ctx.ui().setQueueHidden(false));
            case "FASTGHOSTS" -> ghostSpeed("Faster ghosts", 20, 2.0);
            case "SCORELOSS" -> {int points = SCORE_LOSS * CBLProject.getBoard().getLevel();
                yield Effect.instant("You lose " + points + " points!",
                        ctx -> ctx.mechanics().addScore(-points));
            }
            case "ROTATELOCK" -> new Effect("Rotate lock", 3,
                    ctx -> ctx.movement().blockRotate(),
                    ctx -> ctx.movement().unblockRotate());
            case "JAMMED" -> new Effect("Hard drop jammed", 15,
                    ctx -> ctx.movement().blockHardDrop(),
                    ctx -> ctx.movement().unblockHardDrop());
            case "EXTRAGHOST" -> Effect.instant("Extra ghost",
                    ctx -> ctx.ui().spawnExtraGhost());
            case "GARBAGE" -> Effect.instant("Garbage row", ctx -> {
                ctx.board().GarbageRow(GARBAGE_COLOR);
                ctx.movement().pushUpIfBlocked();
            });
            case "OSTAIR" -> Effect.instant("Bad Pieces", ctx -> {
                Random r = new Random();
                int r1 = r.nextInt(10);
                if (r1 % 2 == 0) {
                    ctx.bag().addBlock("bigO");
                }
                if (r1 % 2 == 1) {
                    ctx.bag().addBlock("stair");
                }
            });    
            default -> null;
        };
        return effect.asDebuff();
    }
}