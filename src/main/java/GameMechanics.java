public class GameMechanics {
    private final Board board;
    private static int score;
    private CBLProject frame;
    private double scoreMultiplier = 1.0;
     
    public GameMechanics(Board board, CBLProject frame) {
        this.board = board;
        this.frame = frame;
    }
     
    public int clearLinesAndScore() {
        int cleared = board.checkRows(0, board.getBoardLength() - 1);
        int level = board.getLevel();
        int points = switch (cleared) {
            case 1 -> 100 * level;
            case 2 -> 300 * level;
            case 3 -> 500 * level;
            case 4 -> 800 * level;
            default -> 0;
        };
        score += (points * scoreMultiplier);
        frame.setScoreText(score);
        frame.setLevelText(board.getLevel());
        return score;
    }
    
    public void multiplyScore(double factor) {
        scoreMultiplier *= factor; 
    }
        
    public static int getScore() {
        return score;
    }
    
    public void addScore(int points) {
        score = Math.max(0, score + points);
        frame.setScoreText(score);
    }
}