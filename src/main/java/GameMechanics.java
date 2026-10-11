public class GameMechanics {
    private final Board board;
    
    public GameMechanics(Board board) {
        this.board = board;
    }
    
    public void clearLines() {
        board.checkRows(0, board.getBoardLength() - 1);
    }
}    
