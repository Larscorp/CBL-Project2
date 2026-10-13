import javax.swing.*;

public class Tetris {
    public static final int START_ROW = 0;
    public static final int START_COL = 3;

    private static Board board;
    private static Bag bag;
    private static Movement movement;
    private static BoardPanel panel;
    private static Timer timer;
    private static boolean gameOver = false;
    public static boolean canHold;
    private static GameMechanics gameMechanics;
    private static int score = 0;
    
    public static void main(String[] args) {
        board = new Board();
        bag = new Bag();
        panel = new BoardPanel(board);
        movement = new Movement(board, bag, bag.getNextBlockQueue(), START_ROW, START_COL);
        Controls controls = new Controls(panel);
        gameMechanics = new GameMechanics(board);
        
        controls.bind("LEFT",  () -> movement.moveLeft());
        controls.bind("RIGHT", () -> movement.moveRight());
        controls.bind("DOWN",  () -> movement.softDrop());
        controls.bind("UP",    () -> movement.rotateClockwise());
        controls.bind("Z",     () -> movement.rotateCounterClockwise());
        controls.bind("U",     () -> movement.rotate180());
        controls.bind("C",     () -> movement.putHold());
        controls.bind("SPACE", () -> {
                movement.hardDrop(); lockAndSpawn();
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
        System.out.println(gameMechanics.ClearLinesAndScore()); //print is debug
        Blocks next = bag.getNextBlockQueue();
        movement.returnHold();
        
        if (!movement.canSpawn(next, START_ROW, START_COL)) {
            gameOver = true;
            panel.setCurrentPiece(null, 0, 0);
            board.printBoard(); //debug
            timer.stop();
            System.out.println("Game over loser");
            return;
        }
        movement.spawnNewPiece(next, START_ROW, START_COL);
    }
    
    private static void refresh() {
        panel.setCurrentPiece(movement.getCurrentPiece(),movement.getPieceRow(),
            movement.getPieceCol());
    }
}