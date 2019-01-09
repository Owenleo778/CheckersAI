
/**
 * An AI using min-max and alpha beta-pruning to play checkers. Always takes the role of black
 */
public class CheckerMinMax {

    private final static int maxDepth = 4;
    private Board board;

    public CheckerMinMax(){
        board = null;
    }

    public void takeTurn(Board b){
        board = new Board(b);

    }

    private Board maxValue(Board board, int depth){
        if (depth == 0){
            return board;
        }
        Integer v = Integer.MIN_VALUE;
        for
    }

    private Piece[][] minValue(Piece[][], int depth){

    }
}