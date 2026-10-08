public class Movement {
    
    Board board = new Board();
    private int level;
    private long lastDropTime = System.nanoTime();
    
    public double fallSpeed() {
        level = board.getLevel();
        return Math.pow((0.8 - ((level - 1) * 0.007)), level - 1);
        //determines the amount of seconds that you have before a block reaches the bottom
    }
    public boolean gravityDrops() {
        long now = System.nanoTime();
        long timeElapsed = now - lastDropTime;
        
        if (timeElapsed >= fallSpeed() * 1000000000) {
            lastDropTime = now;
            return true;
        }
        return false;
     } 
    //determines whether or not the piece drops or not
    
    public void movePieceDown() {
        if (!gravityDrops()) {
            return;
        }
        //checks if it can drop
        
        //moves coordinates downs
            
        }
}
