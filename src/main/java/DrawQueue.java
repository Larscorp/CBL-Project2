import javax.swing.*;
import java.awt.*;

public class DrawQueue extends JPanel {
    private Blocks queuePiece;
    private final int panelSize = 40;
    
    public DrawQueue() {
        setBackground(Color.BLACK);
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (queuePiece == null) return;
        int CELL_SIZE = (panelSize / queuePiece.getSize());
        boolean grid[][] = queuePiece.getGrid();
        int color = queuePiece.getColor();
        for (int i = 0; i < queuePiece.getSize(); i++) {
            for (int j = 0; j < queuePiece.getSize(); j++) {
                if (!grid[i][j]) {
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
    
    public void setQueuePiece(Blocks queuePiece) {
        this.queuePiece = queuePiece;
        setPreferredSize(new Dimension(panelSize, panelSize));
        revalidate();
        repaint();
    }
}
