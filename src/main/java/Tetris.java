import javax.swing.*;

public class Tetris {
    private static final int START_ROW = 0;
    private static final int START_COL = 3;

    private static Board board;
    private static Bag bag;
    private static Movement movement;
    private static BoardPanel panel;
    private static Timer timer;

    public static void main(String[] args) {
        board = new Board();
        bag = new Bag();
        panel = new BoardPanel(board);
        movement = new Movement(board, bag.getNextBlock(), START_ROW, START_COL);
        Controls controls = new Controls(panel);
        
        controls.bind("LEFT",  () -> movement.moveLeft());
        controls.bind("RIGHT", () -> movement.moveRight());
        controls.bind("DOWN",  () -> movement.softDrop());
        controls.bind("SPACE", () -> {movement.hardDrop(); lockAndSpawn();
        });

        JFrame frame = new JFrame("Tetris");
        frame.add(panel);
        frame.pack();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        refresh();

        timer = new Timer(10, e -> {
            if (movement.applyGravity()) {
                lockAndSpawn();
            }
            refresh();
        });
        timer.start();
    }

    private static void lockAndSpawn() {
        movement.lockPiece();
        movement.spawnNewPiece(bag.getNextBlock(), START_ROW, START_COL);
    }

    private static void refresh() {
        panel.setCurrentPiece(
                movement.getCurrentPiece(),
                movement.getPieceRow(),
                movement.getPieceCol());
    }
}