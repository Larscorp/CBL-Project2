import java.util.ArrayList;
import java.util.Collections;

public class Bag {

    private ArrayList<String> blocks = new ArrayList<>();
    private Blocks[] queue = new Blocks[5];
    
    public Bag() {
        refill();
        fillQueue(5);
    }
    
    public void refill() {
        
        blocks.add("I");
        blocks.add("O");
        blocks.add("T");
        blocks.add("J");
        blocks.add("L");
        blocks.add("S");
        blocks.add("Z");
        blocks.add("I");
        blocks.add("O");
        blocks.add("T");
        blocks.add("J");
        blocks.add("L");
        blocks.add("S");
        blocks.add("Z");
        //blocks.add("debugT");
       
        Collections.shuffle(blocks);
    }
    
    private Blocks getNextBlock() {
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
            case "debugT" -> new Blocks(Blocks.debugT, 7);
            case "bigO" -> new Blocks(Blocks.bigO, 3);
            case "neutralFace" -> new Blocks(Blocks.neutralFace, 1);
            case "stair" -> new Blocks(Blocks.stair, 1);
            case "H" -> new Blocks(Blocks.H, 1);
            case "N" -> new Blocks(Blocks.N, 1);
            default -> null;
        };
    }
    
    public void fillQueue(int n) {
        for (int m = 0; m < n; m++) {
            queue[5 - n + m] = getNextBlock();
        }
    }
    
    public Blocks getNextBlockQueue() {
        Blocks tempPiece = queue[0];
        for (int m = 0; m < 4; m++) {
            queue[m] = queue[m + 1];
        }
        fillQueue(1);
        return tempPiece;
    }
    
    public void addBlock(String block) {
        switch (block) {
            case "neutralFace" -> blocks.add("neutralFace");
            case "debugT" -> blocks.add("debugT");
            case "stair" -> blocks.add("stair");
            case "bigO" -> blocks.add("bigO");
            case "N" -> blocks.add("N");
            case "H" -> blocks.add("H");
            case "I" -> blocks.add("I");
            case "O" -> blocks.add("O");
            case "T" -> blocks.add("T");
            case "J" -> blocks.add("J");
            case "L" -> blocks.add("L");
            case "S" -> blocks.add("S");
            case "Z" -> blocks.add("Z");
            default -> System.out.println("Unkown Piece");
        }
        Collections.shuffle(blocks);
    }
    
    public Blocks checkQueue(int n) {
        return queue[n - 1];
    }
    
    public Blocks[] getQueue() {
        return queue;
    }
}