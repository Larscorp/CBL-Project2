public class Board {
    private int boardLength = 30;
    private int boardWidth = 10;
    private int[][] grid;
    
    private int linesCleared;
    
    public Board() {
        this.grid = new int[boardLength][boardWidth];
    }
    
    public void fillSquare(int r, int c, int color) {
        grid[r - 1][c - 1] = color;
    } // -1 for translation between coordinates and indexes
    
    public int checkRows(int rowBegin, int rowEnd) {
        int amount = 0;
        for (int n = rowBegin; n <= rowEnd; n++) {
            int check = 0;
            for (int m = 0; m < boardWidth; m++) {
                if (grid[rowBegin][m] != 0) {
                    check++;
                }
            }
            if (check == boardWidth) {
                amount++;
                deleteRows(n);
            }
        }
        return amount;
    }
    
    public void deleteRows(int r) {
        for (int n = boardLength - 1; n > r; n--) {
            System.arraycopy(grid[n], 0, grid[n - 1], 0, boardLength);
        }
    }
    
    public void printBoard() {
        for (int n = boardLength - 1; n >= 0; n--) {
            for (int m = 0; m < boardWidth; m++) { 
                System.out.print(grid[n][m] + " ");
            }
            System.out.println();
        }
    }
    
    public boolean hasColision(int r, int c) {
        if (r > boardLength || r <= 0 || c > boardWidth || c <= 0) {
            return true; // check if the given coordinates are within the board
        } else {
            return grid[r - 1][c - 1] != 0;
        }
    }
    
    /*public boolean shouldPlace(int r, int c, Blocks currentBlock) {
        currentBlock[1][2] = true;
    }*/
    
    public int getLevel() {
        return (linesCleared / 10) + 1;
    }
    
    public int[][] getGrid() {
        return grid;
    } 
    
    public int getBoardWidth() {
        return boardWidth;
    }
    
    public int getBoardLength() {
        return boardLength;
    }
}
