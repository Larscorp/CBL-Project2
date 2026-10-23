import java.awt.*;
import javax.swing.*;      
        
public class BoardPanel extends JPanel {
    
    private Board board;
    private int cellSize = 20;
    
    private Blocks currentPiece;
    private int pieceRow;
    private int pieceCol;
    private int ghostRow;
    private boolean ghostVisible = true;
    
    public BoardPanel() {
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (board == null) {
            return;
        }
        int[][] grid = board.getGrid();
        
        for (int i = 0; i < board.getBoardLength(); i++) {
            for (int j = 0; j < board.getBoardWidth(); j++) {
                int color = grid[i][j];
                
                if (color == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize); 
                } else {
                    g.setColor(getBlockColor(color));
                    g.fillRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
                if (i >= 2) {
                    g.setColor(Color.DARK_GRAY);
                    g.drawRect(j * cellSize, i * cellSize, cellSize, cellSize);
                }
            }
        }
        
        if (currentPiece != null) {
            boolean[][] pieceGrid = currentPiece.getGrid();
            int color = currentPiece.getColor();
            if (ghostVisible) {
                for (int row = 0; row < pieceGrid.length; row++) {
                    for (int col = 0; col < pieceGrid[row].length; col++) {

                        if (!pieceGrid[row][col]) {
                            continue;
                        }
                        int boardRow = ghostRow + row;
                        int boardCol = pieceCol + col;

                        if (boardRow < 0 || boardRow >= board.getBoardLength() || boardCol < 0
                            || boardCol >= board.getBoardWidth()) {
                            continue;
                        }
                        Color c = getBlockColor(color);
                        g.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), 80));
                        g.fillRect(boardCol * cellSize, boardRow * cellSize, cellSize, cellSize);
                    }
                }  
            }
            for (int row = 0; row < pieceGrid.length; row++) {
                for (int col = 0; col < pieceGrid[row].length; col++) {

                    if (!pieceGrid[row][col]) {
                        continue;
                    }
                    int boardRow = pieceRow + row;
                    int boardCol = pieceCol + col;

                    if (boardRow < 0 || boardRow >= board.getBoardLength() || boardCol < 0
                        || boardCol >= board.getBoardWidth()) {
                        continue;
                    }
                    g.setColor(getBlockColor(color));

                    g.fillRect(boardCol * cellSize,
                            boardRow * cellSize,
                            cellSize,
                            cellSize
                    );
                    g.setColor(Color.WHITE);

                    g.drawRect(boardCol * cellSize,
                        boardRow * cellSize,
                        cellSize,
                        cellSize
                    );
                }
            }
        }
    }
    
    private Color getBlockColor(int color) {
        return switch (color) {
            case 1 -> Color.RED;
            case 2 -> Color.ORANGE; 
            case 3 -> Color.YELLOW;
            case 4 -> Color.GREEN;
            case 5 -> Color.CYAN;
            case 6 -> Color.BLUE;
            case 7 -> Color.MAGENTA;
            case 8 -> Color.GRAY;
            case 9 -> Color.lightGray;
            default -> Color.BLACK;
        };
    }
    
    public void setCurrentPiece(Blocks piece, int row, int col) {
        this.currentPiece = piece;
        this.pieceRow = row;
        this.pieceCol = col;
        
        repaint();
    }
    
    public void setBoard(Board board) {
        this.board = board;
        int screenHeight = Toolkit.getDefaultToolkit().getScreenSize().height;
        cellSize = Math.max(15, Math.min(35, (screenHeight - 350) / board.getBoardLength()));

        setPreferredSize(new Dimension(board.getBoardWidth() * cellSize,
            board.getBoardLength() * cellSize));
        revalidate();
        repaint();
    }
    
    public void setGhostVisible(boolean visible) { 
        ghostVisible = visible; 
    }
    
    public void setGhostRow(int row) {
        this.ghostRow = row;
    }
} 