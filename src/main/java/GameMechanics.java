import java.util.ArrayList;

public class GameMechanics {
    private final Board board;
    private static int score;
    private CBLProject frame;
     
    public GameMechanics(Board board, CBLProject frame) {
        this.board = board;
        this.frame = frame;
    }
     
    public int ClearLinesAndScore() {
        int cleared = board.checkRows(0, board.getBoardLength() - 1);
        int level = board.getLevel();
        score += switch (cleared) {
            case 1 -> 100 * level;
            case 2 -> 300 * level;
            case 3 -> 500 * level;
            case 4 -> 800 * level;
            default -> 0;
        };
        frame.setScoreText(score);
        frame.setLevelText(board.getLevel());
        return score;
    }
        
    public static int getScore() {
        return score;
    }
}
