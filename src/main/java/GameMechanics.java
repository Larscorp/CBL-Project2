public class GameMechanics {
     private final Board board;
     private int score;
     
     public GameMechanics(Board board) {
         this.board = board;
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
        return score;
     }
     
     public int getScore() {
         return score;
     }
}
