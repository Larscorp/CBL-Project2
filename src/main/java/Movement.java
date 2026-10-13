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
    
    public Movement(Board board, Bag bag, Blocks currentPiece, int startRow, int startCol) {
        this.board = board;
        this.bag = bag;
        this.currentPiece = currentPiece;
        this.pieceRow = startRow;
        this.pieceCol = startCol;
    }
    
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
     } //determines whether or not the piece drops or not
    
    private boolean canMove(boolean[][] shape, int row, int col) {
        for (int r = 0; r < shape.length; r++) {
            for (int c = 0; c < shape[r].length; c++) {
                if (!shape[r][c]) continue;
                if (board.hasColision(row + r + 1, col + c + 1)) return false;
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
        return tryMove(0, -1);
    }
    
    public boolean moveRight() {
        return tryMove (0, 1);
    }
    
    public boolean softDrop() {
        return tryMove(1, 0);
    }
    
    public void hardDrop() {
        while (tryMove (1, 0)){
        }
    }
    
    public boolean rotateClockwise() {
        currentPiece.simpleRotateBlockClockwise();
        if (canMove(currentPiece.getGrid(), pieceRow, pieceCol)) {
            return true;
        }
        currentPiece.simpleRotateBlockCounterClockwise();
        return false;
    }
    
    public boolean rotateCounterClockwise() {
        currentPiece.simpleRotateBlockCounterClockwise();
        if (canMove(currentPiece.getGrid(), pieceRow, pieceCol)) {
            return true;
        }
        currentPiece.simpleRotateBlockClockwise();
        return false;
    }
    
    //debug
    public boolean rotate180() {
        currentPiece.simpleRotate180();
        if (canMove(currentPiece.getGrid(), pieceRow, pieceCol)) {
            return true;
        }
        currentPiece.simpleRotate180();
        return false;
    }
    
    public boolean applyGravity() {
        if (!gravityDrops()) return false;
        return !tryMove(1, 0);
    }
    
    public void lockPiece() {
        currentPiece.placePiece(pieceRow, pieceCol, board);
    }
    
    public void spawnNewPiece(Blocks piece, int row, int col) {
        currentPiece = piece;
        pieceRow = row;
        pieceCol = col;
        lastDropTime = System.nanoTime();
    }
    
    public void putHold() {
        if (!canHold) return;
        canHold = false;
        
        if (holdPiece == Blocks.Empty) {
            holdPiece = currentPiece;
            spawnNewPiece(bag.getNextBlockQueue(), Tetris.START_ROW, Tetris.START_COL);
        } else {
            Blocks tempPiece = currentPiece;
            currentPiece = holdPiece;
            holdPiece = tempPiece;
            pieceRow = Tetris.START_ROW;
            pieceCol = Tetris.START_COL;
            lastDropTime = System.nanoTime();
        }
        
    }
    
    public void clearLines() {
        board.checkRows(0, board.getBoardLength() - 1);
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
    
    public Blocks getCurrentPiece() {
        return currentPiece;
    }
    
    public int getPieceRow() {
        return pieceRow;
    }
    
    public int getPieceCol() {
        return pieceCol;
    }
}
