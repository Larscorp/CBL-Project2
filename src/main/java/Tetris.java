import javax.swing.*;

public class Tetris {
      public static void main(String[] args) {
         SwingUtilities.invokeLater(() -> {
            Board board = new Board();
            TetrisGUI gui = new TetrisGUI(board);
            JFrame frame = new JFrame("Tetris");

            frame.add(gui);
            frame.pack();

            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLocationRelativeTo(null);
            frame.setResizable(false);
            frame.setVisible(true);
        });
    }
}
