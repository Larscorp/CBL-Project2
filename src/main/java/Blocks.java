/*
Defines all tetraminoes.

Colors are defined as follows:
1: red
2: orange
3: yellow
4: green
5: light blue
6: dark blue
7: purple
8: grey (possible use as garbage/permanent lines
*/
import java.util.Arrays;

public class Blocks {
    private boolean[][] grid;
    private int color;
    private int[][] bottomList;
    
    public Blocks(int size, int col) { 
        this.grid = new boolean[size][size];
        this.color = col;
        this.bottomList = new int[(size + 1) / 2][size];
    }
    
    public Blocks(boolean[][] grid, int col) {
        this.grid = grid;
        this.color = col;
        this.bottomList = new int[(grid[0].length + 1) / 2][grid[0].length];
    }
    
    public Blocks(Blocks piece, int col) {
        this.grid = new boolean[piece.grid.length][piece.grid.length];
        this.color = col;
        this.bottomList = new int[(grid[0].length + 1) / 2][grid[0].length];
        for (int n = 0; n < piece.grid.length; n++) {
            for (int m = 0; m < piece.grid.length; m++) {
                this.grid[n][m] = piece.grid[n][m];
            }
        }
    }
    
    public static Blocks T = new Blocks(new boolean[][] {
        {false, true,  false},
        {true,  true,  true},
        {false, false, false}
    }, 7); // purple

    public static Blocks J = new Blocks(new boolean[][] {
        {true,  false, false},
        {true,  true,  true},
        {false, false, false}
    }, 6); // dark blue

    public static Blocks S = new Blocks(new boolean[][] {
        {false, true,  true},
        {true,  true,  false},
        {false, false, false}
    }, 4); // green

    public static Blocks Z = new Blocks(new boolean[][] {
        {true,  true,  false},
        {false, true,  true},
        {false, false, false}
    }, 1); // red
    
    public static Blocks O = new Blocks(new boolean[][] {
        {true, true,  false},
        {true, true,  false},
        {false, false, false}
    }, 3); // yellow

    public static Blocks L = new Blocks(new boolean[][] {
        {false, false, true},
        {true,  true,  true},
        {false, false, false}
    }, 2); // orange
    
    public static Blocks I = new Blocks(new boolean[][] {
        {false, false, false, false},
        {true, true, true, true},
        {false, false, false, false},
        {false, false, false, false}
    }, 5); // light blue
    
    public static Blocks debugT = new Blocks(new boolean[][] {
        {false,  true,  true,  true,  true,  true,  true,  false  },
        {true,  true,  true,  true,  true,  true,  true,  true  },
        {true,  false, false, true,  true,  false, false, true  },
        {false, false, false, true,  true,  false, false, false },
        {false, false, false, true,  true,  false, false, false },
        {false, false, false, true,  true,  false, false, false },
        {false, false, true,  true,  true,  true,  false, false },
        {false, true,  true,  true,  true,  true,  true,  false }
    }, 7); // debug piece, purple
    
    public void simpleRotateBlockCounterClockwise() { // Counterclockwise matrix rotation
        int size = grid[0].length;
        boolean[][] newRotation = new boolean[size][size];
        for (int n = 0; n < size; n++) {
            for (int m = 0; m < size; m++) { 
                newRotation[size - m - 1][n] = grid[n][m];
            }
        }
        grid = newRotation;
        checkBottoms();
    }
    
    public void simpleRotateBlockClockwise() { // Clockwise matrix rotation.
        int size = grid[0].length;
        boolean[][] newRotation = new boolean[size][size];
        for (int n = 0; n < size; n++) {
            for (int m = 0; m < size; m++) { 
                newRotation[m][size - n - 1] = grid[n][m];
            }
        }
        grid = newRotation;
        checkBottoms();
    }
    
    public void simpleRotate180() { // 180 matrix rotation
        int size = grid[0].length;
        boolean[][] newRotation = new boolean[size][size];
        for (int n = 0; n < size; n++) {
            for (int m = 0; m < size; m++) { 
                newRotation[size - n - 1][size - m - 1] = grid[n][m];
            }
        }
        grid = newRotation;
        checkBottoms();
    }
    
    public void printPiece() { // debug method
        int size = grid[0].length;
        for (int n = 0; n < size; n++) {
            for (int m = 0; m < size; m++) { 
                System.out.print(grid[n][m] + " ");
            }
            System.out.println();
        }
    }
    
    public void placePiece(int r, int c, Board board) { // Input starts Top left corner 
        int size = grid[0].length;
        for (int n = 0; n < size; n++) {
            for (int m = 0; m < size; m++) {
                if (grid[n][m]) { 
                    board.fillSquare((r - n), (c + m), color);
                } 
            }
        }
    }
    
    public void checkBottoms() {
        int size = grid[0].length;
        for (int n = 0; n < bottomList.length; n++) {
            Arrays.fill(bottomList[n], -1);
        }
        for (int n = 0; n < size; n++) {
            int overhangs = 0;
            if (grid[n][size - 1]) {
                bottomList[overhangs][n] = size - 1;
                overhangs++;
            }
            for (int m = size - 2; m >= 0; m--) {
                if ((grid[n][m]) && (!grid[n][m + 1])) {
                    bottomList[overhangs][n] = m;
                    overhangs++;
                }
            }
            if (overhangs == 0) {
                bottomList[overhangs][n] = -1;
            }
        }
    }
    
    public int[][] getBottoms() {
        int[][] newBottoms = new int[grid.length][grid[0].length];
        for (int n = 0; n < grid.length; n++) {
            System.arraycopy(bottomList[n], 0, newBottoms[n], 0, bottomList[0].length);
        }
        return newBottoms;
    }
    
    public int getColor() {
        return color;
    }
    
    public boolean[][] getGrid() {
        return grid;
    }
}