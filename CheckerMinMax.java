/**
 * An AI using min-max and alpha beta-pruning to play checkers. Always takes the role of black
 */
public class CheckerMinMax {

    private final static int maxDepth = 2;

    public CheckerMinMax(){
    }

    public void takeTurn(Board board){
        Board b1 = null;
        int v = -1000;
        for (Board b2 : board.allMoves(Board.AIPLAYER)){
            int temp = Integer.max(v, minValue(b2, 1));
            if (v < temp){
                v = temp;
                b1 = b2;
            }
        }
        board.copyState(b1);
    }

    /*
    private void movePiece(int x1, int y1, int x2, int y2){
        board.board[x2][y2] = board.board[x1][y1];
        board.board[x1][y1] = null;
        board.board[x2][y2].setPosition(new Point(x2,y2));
    }
    */

    private int maxValue(Board board, int depth){
        if (depth >= maxDepth){
            return evaluation(board);
        }
        Integer v = -1000;
        for (Board b : board.allMoves(Board.AIPLAYER)){
            v = Integer.max(v, minValue(b, depth++));
        }
        return v;
    }

    private int minValue(Board board, int depth){
        if (depth >= maxDepth){
            return evaluation(board);
        }
        Integer v = 1000;
        for (Board b : board.allMoves(Board.AIPLAYER * -1)){
            v = Integer.min(v, maxValue(b, depth++));
        }
        return v;
    }

    private int evaluation(Board board) {
        int total = 0;
        if (board != null) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Piece p = board.board[x][y];
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
            total = Integer.MIN_VALUE;
        }
        return total;
    }

}