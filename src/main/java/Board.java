public class Board {
    private int boardLength = 30;
    private int boardWidth = 10;
    private int[][] grid;
    
    public Board() {
        this.grid = new int[boardLength][boardWidth];
    }
    
    public void fillSquare(int r, int c, int color) {
        grid[r][c] = color;
    }
    
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
   
}
