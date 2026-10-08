import javax.swing.*;

public class Tetris {
      public static void main(String[] args) {
        Board board = new Board();

        Bag bag = new Bag();
        Blocks currentPiece = bag.getNextBlock();
        BoardPanel panel = new BoardPanel(board);
        panel.setCurrentPiece(currentPiece, 0, 3);

        JFrame frame = new JFrame("Tetris");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        //debug for fallSpeed
        Movement movement =  new Movement();
        double speed = movement.fallSpeed();
        System.out.println(speed);
        }
    }