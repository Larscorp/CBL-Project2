import javax.swing.*;

public class Tetris {
    public static void main(String[] args) {
        Board board = new Board();

        Bag bag = new Bag();
        Blocks currentPiece = bag.getNextBlock();

        BoardPanel panel = new BoardPanel(board);

        int startRow = 0;
        int startCol = 3;

        panel.setCurrentPiece(currentPiece, startRow, startCol);

        JFrame frame = new JFrame("Tetris");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Movement movement =
                new Movement(board, currentPiece, startRow, startCol);

        Timer timer = new Timer(10, e -> {
            movement.movePieceDown();

            // You'll need a getter for pieceRow/pieceCol
            panel.setCurrentPiece(
                    currentPiece,
                    movement.getPieceRow(),
                    movement.getPieceCol()
            );

            panel.repaint();
        });

        timer.start();
    }
}