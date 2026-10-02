public class Tetris {
      public static void main(String[] args) {
        Bag bag = new Bag();
        for (int i = 0; i < 7 ; i++) {
            Blocks currentBlock = bag.getNextBlock();
            currentBlock.printPiece();
            //print hello
        }
    }
}
