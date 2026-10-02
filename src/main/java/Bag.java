import java.util.ArrayList;
import java.util.Collections;

public class Bag {

    private ArrayList<String> blocks = new ArrayList<>();
    
    public Bag() {
        refill();
    }
    public void refill() {
        
        blocks.add("I");
        blocks.add("O");
        blocks.add("T");
        blocks.add("J");
        blocks.add("L");
        blocks.add("S");
        blocks.add("Z");
       
        Collections.shuffle(blocks);
    }
    
    public Blocks getNextBlock() {
        if (blocks.isEmpty()) {
            refill();
        }
        String next = blocks.remove(0);
        
        return switch (next) {
            case "I" -> new Blocks(Blocks.I, 5);
            case "O" -> new Blocks(Blocks.O, 3);
            case "T" -> new Blocks(Blocks.T, 7);
            case "J" -> new Blocks(Blocks.J, 6);
            case "L" -> new Blocks(Blocks.L, 2);
            case "S" -> new Blocks(Blocks.S, 4);
            case "Z" -> new Blocks(Blocks.Z, 1);
            default -> null;
        };
    }
}
    