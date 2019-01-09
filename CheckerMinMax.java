/**
 * An AI using min-max and alpha beta-pruning to play checkers. Always takes the role of black
 */
public class CheckerMinMax {

    private final static int maxDepth = 10;
    private Board board;

    public CheckerMinMax(Board b){
        board = b;
    }

    public void takeTurn(BoardState bS){
        BoardState b1 = null;
        int v = -1000;
        for (BoardState b2 : board.allMoves(bS,Board.AIPLAYER)){
            int temp = Integer.max(v, minValue(b2, 1));
            if (v < temp){
                v = temp;
                b1 = b2;
            }
        }

        // Sometimes b1 is never overwritten? Look into,
        //causes null pointer exception \/


        board.copyState(b1);
    }

    private int maxValue(BoardState bS, int depth){
        if (depth >= maxDepth){
            return evaluation(bS);
        }
        Integer v = -1000;
        for (BoardState b : board.allMoves(bS, Board.AIPLAYER)){
            v = Integer.max(v, minValue(b, depth++));
        }
        return v;
    }

    private int minValue(BoardState bS, int depth){
        if (depth >= maxDepth){
            return evaluation(bS);
        }
        Integer v = 1000;
        for (BoardState b : board.allMoves(bS,Board.AIPLAYER * -1)){
            v = Integer.min(v, maxValue(b, depth++));
        }
        return v;
    }

    private int evaluation(BoardState b) {
        int total = 0;
        if (b != null) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Piece p = b.board[x][y];
                    if (p != null) {
                        if (p.isKing()) {
                            if (p.getColour() == Board.AIPLAYER) {
                                total++;
                                total++;
                            } else {
                                total--;
                                total--;
                            }
                        } else {
                            if (p.getColour() == Board.AIPLAYER) {
                                total++;
                            } else {
                                total--;
                            }
                        }
                    }
                }
            }
        } else {
            total = -1000;
        }
        return total;
    }

}