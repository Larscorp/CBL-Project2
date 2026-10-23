import java.util.Arrays;
import java.util.Random;    

public class Board {
    private int boardLength = 22;
    private int boardWidth = 10;
    private int[][] grid; //row 0 is top of the board.
    private final Random random = new Random();
    
    private int linesCleared;
    
    public Board() {
        this.grid = new int[boardLength][boardWidth];
    }
    
    public void fillSquare(int r, int c, int color) {
        grid[r - 1][c - 1] = color;
    } // -1 for translation between coordinates and indexes
    
    public int checkRows(int rowBegin, int rowEnd) {
        int amount = 0;
        for (int n = 0; n <= rowEnd; n++) {
            int check = 0;
            for (int m = 0; m < boardWidth; m++) {
                if (grid[n][m] != 0) {
                    check++;
                }
            }
            if (check == boardWidth) {
                amount++;
                deleteRows(n);
            }
        }
        linesCleared += amount;
        return amount;
    }
    
    public void deleteRows(int r) {
        for (int n = r; n > 0; n--) {
            System.arraycopy(grid[n - 1], 0, grid[n], 0, boardWidth);
        }
        grid[0] = new int[boardWidth];
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
            return true; 
        } else {
            return grid[r - 1][c - 1] != 0;
        }
    }
    
    public boolean fillHoles(int color) {
        return (fillCovered(color) || fillRows(color));
    }
    
    private boolean fillCovered(int color) {
        int[] top = new int[boardWidth];
        for (int c = 0; c < boardWidth; c++) {
            top[c] = boardLength;
            for (int r = 0; r < boardLength; r++) {
                if (grid[r][c] != 0) {
                    top[c] = r;
                    break;
                }
            }
        }
        for (int r = boardLength - 1; r >= 0; r--) {
            for (int c = 0; c < boardWidth; c++) {
                if (grid[r][c] == 0 && r > top[c]) {
                    grid[r][c] = color;
                    return true;
                }
            }
        }
        return false;
    }

    private boolean fillRows(int color) {
        for (int r = boardLength - 1; r >= 0; r--) {
            int blocks = 0;
            for (int c = 0; c < boardWidth; c++) {
                if (grid[r][c] != 0) {
                    blocks++;
                }
            }
            if (blocks == 0 || blocks == boardWidth) {
                continue;                                  
            }
            for (int c = 0; c < boardWidth; c++) {
                if (grid[r][c] == 0) {
                    grid[r][c] = color;
                    return true;
                }
            }
        }
        return false;
    }
    
    public boolean garbageRow(int color) {
        for (int c = 0; c < boardWidth; c++) {
            if (grid[0][c] != 0) {
                return false;
            }
        }
        for (int r = 0; r < boardLength - 1; r++) {
            grid[r] = grid[r + 1];
        }
        int[] garbage = new int[boardWidth];
        Arrays.fill(garbage, color);
        garbage[random.nextInt(boardWidth)] = 0;
        grid[boardLength - 1] = garbage;
        return true;
    }
    
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
    
    public int getLinesCleared() {
        return linesCleared; 
    }
}