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
            case 5 -> 1500 * level;
            case 6 -> 2800 * level;
            case 7 -> 4000 * level;
            case 8 -> 6700 * level;
            case 9 -> 12300 * level;
            case 10 -> 19600 * level;
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
    
    public void resetScore() {
        score = 0;
        scoreMultiplier = 1.0;
        frame.setLevelText(board.getLevel());
        frame.setScoreText(0);
    }
}