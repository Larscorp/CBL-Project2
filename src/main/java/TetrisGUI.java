import javax.swing.*;
import java.awt.*;       
        
public class TetrisGUI extends JPanel {
    
    private Board board;
    private final int CELL_SIZE = 30;
    
    public TetrisGUI (Board board) {
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
                
            }
        }
    }
}
