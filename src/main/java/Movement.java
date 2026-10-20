
public class Movement {
    
    private final Board board;
    private int level;
    private long lastDropTime = System.nanoTime();
    private final Bag bag;

    private int pieceRow;
    private int pieceCol;
    private Blocks currentPiece;
    private Blocks holdPiece = Blocks.Empty;
    private boolean canHold = true;
    private static final long LOCK_DELAY =1_000_000_000L; //lock delay in nanoseconds
    private static final int MAX_RESETS = 5; //max amount of stall time
    private boolean grounded = false;   
    private long lockStart;             
    private int lockResets = 0; //how many times moved on the ground
    private double fallDelayMultiplier = 1.0;
    private int holdBlockers = 0;
    private int state = 0;
    
    public Movement(Board board, Bag bag, Blocks currentPiece, int startRow, int startCol) {
        this.board = board;
        this.bag = bag;
        this.currentPiece = currentPiece;
        this.pieceRow = startRow;
        this.pieceCol = startCol;
    }
    
    public double fallSpeed() {
        level = board.getLevel();
        return (Math.pow((0.8 - ((level - 1) * 0.007)), (level - 1) / 5 + 1) * fallDelayMultiplier);
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
    } //determines whether or not the piece drops or not
    
    private boolean canMove(boolean[][] shape, int row, int col) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (!shape[r][c]) {
                    continue;
                }
                if (board.hasColision(row + r + 1, col + c + 1)) {
                    return false;
                }
            }
        }
        return true;
    }
    
    public boolean canCurrentlyMove() {
        return canMove(currentPiece.getGrid(), pieceRow, pieceCol);
    }
    
    private boolean tryMove(int dRow, int dCol) {
        if (canMove(currentPiece.getGrid(), pieceRow + dRow, pieceCol + dCol)) {
            pieceRow += dRow;
            pieceCol += dCol;
            return true;
        }
        return false;
    }
    
    public boolean moveLeft() {
        boolean moved = tryMove(0, -1);
        if (moved) {
            resetLockTimer();
        }
        return moved;
    }
    
    public boolean moveRight() {
        boolean moved = tryMove(0, 1);
        if (moved) {
            resetLockTimer();
        }
        return moved;
    }
    
    public boolean softDrop() {
        boolean moved = tryMove(1, 0);
        if (moved) {
            resetLockTimer();
        }
        return moved;
    }
    
    public void hardDrop() {
        while (tryMove(1, 0)){
        }
    }
    
    public boolean rotateClockwise() {
        System.out.println(state);
        currentPiece.simpleRotateBlockClockwise();
        if (canMove(currentPiece.getGrid(), pieceRow, pieceCol)) {
            state++;
            return true;
        } else if (currentPiece.getSize() == 3) {
            switch (((state % 4) + 4) % 4) {
                case 0:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        state++;
                        pieceCol--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol - 1)) {
                        pieceRow--;
                        pieceCol--;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol)) {
                        pieceRow = pieceRow + 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol - 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol--;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
                case 1:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        state++;
                        pieceCol++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol + 1)) {
                        pieceRow++;
                        pieceCol++;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol)) {
                        pieceRow = pieceRow - 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol + 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol++;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
                case 2:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        state++;
                        pieceCol++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol + 1)) {
                        pieceRow--;
                        pieceCol++;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol)) {
                        pieceRow = pieceRow + 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol + 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol++;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
                case 3:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        state++;
                        pieceCol--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol - 1)) {
                        pieceRow++;
                        pieceCol--;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol)) {
                        pieceRow = pieceRow - 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol - 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol--;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
            }
        } else if (currentPiece.getSize() == 4) {
            switch (((state % 4) + 4) % 4) {
                case 0:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 2)) {
                        state++;
                        pieceCol = pieceCol - 2;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        pieceCol++;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol - 2)) {
                        pieceRow++;
                        pieceCol = pieceCol - 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol + 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol++;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
                case 1:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        state++;
                        pieceCol--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 2)) {
                        pieceCol = pieceCol + 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol - 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol--;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol + 2)) {
                        pieceRow++;
                        pieceCol = pieceCol + 2;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
                case 2:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 2)) {
                        state++;
                        pieceCol = pieceCol + 2;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        pieceCol--;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol + 2)) {
                        pieceRow--;
                        pieceCol = pieceCol + 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol - 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol--;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
                case 3:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        state++;
                        pieceCol++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 2)) {
                        pieceCol = pieceCol - 2;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol + 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol++;
                        state++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol - 2)) {
                        pieceRow--;
                        pieceCol = pieceCol - 2;
                        state++;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockCounterClockwise();
                        return false;
                    }
            }
        }
        currentPiece.simpleRotateBlockCounterClockwise();
        return false;
    }
    
    public boolean rotateCounterClockwise() {
        currentPiece.simpleRotateBlockCounterClockwise();
        if (canMove(currentPiece.getGrid(), pieceRow, pieceCol)) {
            state--;
            return true;
        } else if (currentPiece.getSize() == 3) {
            switch (((state % 4) + 4) % 4) {
                case 0:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        state--;
                        pieceCol++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol + 1)) {
                        pieceRow--;
                        pieceCol++;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol)) {
                        pieceRow = pieceRow + 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol + 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol++;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
                case 3:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        state--;
                        pieceCol--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol - 1)) {
                        pieceRow++;
                        pieceCol--;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol)) {
                        pieceRow = pieceRow - 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol - 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol--;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
                case 2:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        state--;
                        pieceCol--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol - 1)) {
                        pieceRow++;
                        pieceCol--;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol)) {
                        pieceRow = pieceRow - 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol - 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol--;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
                case 1:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        state--;
                        pieceCol++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol + 1)) {
                        pieceRow++;
                        pieceCol++;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol)) {
                        pieceRow = pieceRow - 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol + 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol++;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
            }
        } else if (currentPiece.getSize() == 4) {
            switch (((state % 4) + 4) % 4) {
                case 0:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        state--;
                        pieceCol--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 2)) {
                        pieceCol = pieceCol + 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol - 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol--;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol + 2)) {
                        pieceRow++;
                        pieceCol = pieceCol + 2;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
                case 1:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 2)) {
                        state--;
                        pieceCol = pieceCol + 2;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 1)) {
                        pieceCol--;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol + 2)) {
                        pieceRow--;
                        pieceCol = pieceCol + 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol - 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol--;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
                case 2:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        state--;
                        pieceCol++;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 2)) {
                        pieceCol = pieceCol - 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 2, pieceCol + 1)) {
                        pieceRow = pieceRow + 2;
                        pieceCol++;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 1, pieceCol - 2)) {
                        pieceRow--;
                        pieceCol = pieceCol - 2;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
                case 3:
                    if (canMove(currentPiece.getGrid(), pieceRow, pieceCol - 2)) {
                        state--;
                        pieceCol = pieceCol - 2;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow, pieceCol + 1)) {
                        pieceCol++;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol - 2)) {
                        pieceRow++;
                        pieceCol = pieceCol - 2;
                        state--;
                        return true;
                    } else if (canMove(currentPiece.getGrid(), pieceRow - 2, pieceCol + 1)) {
                        pieceRow = pieceRow - 2;
                        pieceCol++;
                        state--;
                        return true;
                    } else {
                        currentPiece.simpleRotateBlockClockwise();
                        return false;
                    }
            }
        }
        currentPiece.simpleRotateBlockClockwise();
        return false;
    }
    
    //debug
    public boolean rotate180() {
        currentPiece.simpleRotate180();
        if (canMove(currentPiece.getGrid(), pieceRow, pieceCol)) {
            state = state + 2;
            return true;
        }
        currentPiece.simpleRotate180();
        return false;
    }
    
    private boolean isGrounded() {
        return !canMove(currentPiece.getGrid(), pieceRow + 1, pieceCol);
    }
    
    public boolean applyGravity() {
        if (isGrounded()) {
            long now = System.nanoTime();
            if (!grounded) {
                grounded = true;
                lockStart = now;
            }
        return now - lockStart >= LOCK_DELAY;    
        }
        grounded = false;
        if (gravityDrops()) {
            tryMove(1,0);
        }
        return false;
    }
    
    private void resetLockTimer() {
        if (grounded && lockResets < MAX_RESETS) {
            lockStart = System.nanoTime();
            lockResets++;
        }
    }
    
    public void lockPiece() {
        currentPiece.placePiece(pieceRow, pieceCol, board);
    }
    
    public void spawnNewPiece(Blocks piece, int row, int col) {
        currentPiece = piece;
        state = 0;
        pieceRow = row;
        pieceCol = col;
        lastDropTime = System.nanoTime();
        grounded = false;
        lockResets = 0;
    }
    
    public void putHold() {
        if (!canHold || holdBlockers > 0) {
            return;
        }
        canHold = false;
        switch (((state % 4) + 4) % 4) {
            case 1 -> currentPiece.simpleRotateBlockCounterClockwise();
            case 2 -> currentPiece.simpleRotate180();
            case 3 -> currentPiece.simpleRotateBlockClockwise();
        }
        
        if (holdPiece == Blocks.Empty) {
            holdPiece = currentPiece;
            currentPiece = bag.getNextBlockQueue();
            spawnNewPiece(currentPiece, 0, (((board.getBoardWidth() + 1) / 2) - 
                    ((currentPiece.getSize() + 1) / 2)));
            
        } else {
            Blocks tempPiece = currentPiece;
            currentPiece = holdPiece;
            holdPiece = tempPiece;
            pieceRow = 0;
            pieceCol = (((board.getBoardWidth() + 1) / 2) - ((currentPiece.getSize() + 1) / 2));
            lastDropTime = System.nanoTime();
        }
        
    }
    
    public int getGhostRow() {
        if (currentPiece == null) {
            return pieceRow;
        }
        boolean[][] shape = currentPiece.getGrid();
        int row = pieceRow;
        while (canMove(shape, row + 1, pieceCol)) {
            row++;
        }
        return row;
    }
        
    public void returnHold() {
        canHold = true;
    }
    
    public boolean fits() {
        return canMove(currentPiece.getGrid(), pieceRow, pieceCol);
    }
    
    public boolean canSpawn(Blocks piece, int row, int col) {
        return canMove(piece.getGrid(), row, col);
    }

    public void multiplyFallDelay(double factor) {
        fallDelayMultiplier *= factor;
    
    }
    public void blockHold() {
        holdBlockers++; 
    }
    
    public void unblockHold() { 
        holdBlockers--; 
    }
    
    public Blocks getCurrentPiece() {
        return currentPiece;
    }
    
    public int getPieceRow() {
        return pieceRow;
    }
    
    public int getPieceCol() {
        return pieceCol;
    }
    
    public Blocks getHoldPiece() {
        return holdPiece;
    }
    
    public boolean usedHold() {
        return !canHold; 
    }
}