import java.awt.*;
import javax.swing.*;

public class TetrisGUI extends JPanel {
    private BoardPanel boardPanel;
    
    public TetrisGUI(Board board) {
        setLayout(new BorderLayout());
        boardPanel = new BoardPanel(board);
        
        add(boardPanel, BorderLayout.CENTER);
    }
}