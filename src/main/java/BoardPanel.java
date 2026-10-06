import javax.swing.*;
import java.awt.*;       
        
public class BoardPanel extends JPanel {
    
    private Board board;
    private final int CELL_SIZE = 25;
    
    public BoardPanel (Board board) {
        this.board = board;
        
        setPreferredSize(new Dimension(
                board.getBoardWidth() * CELL_SIZE,
                board.getBoardLength() * CELL_SIZE));
        
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        int[][] grid = board.getGrid();
        
        for (int i = 0; i < board.getBoardLength(); i++) {
            for (int j = 0; j < board.getBoardWidth(); j++) {
                int color = grid[i][j];
                
                if (color == 0) {
                    g.setColor(Color.BLACK);
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE); 
               } else {
                    g.setColor(getBlockColor(color));
                    g.fillRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                }
                
                g.setColor(Color.DARK_GRAY);
                g.drawRect(j * CELL_SIZE, i * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                
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
            default -> Color.BLACK;
        };
    }
} 
