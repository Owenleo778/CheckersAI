import java.util.ArrayList;
import java.util.Collections;

/**
 * An AI using min-max and alpha beta-pruning to play checkers. Always takes the role of black
 * @author Owen Salvage
 */
public class CheckerMinMax implements Runnable{

    private int maxDepth;
    private Board board;
    private int colour;

    public CheckerMinMax(Board b, int depth){
        board = b;
        colour = b.aiPlayer;
        maxDepth = depth;
    }

    public CheckerMinMax(Board b, int colour, int depth){
        this(b, depth);
        this.colour = colour;
    }

    @Override
    public void run() {
        takeTurn(board.getBoardState());
    }

    public void takeTurn(BoardState bS){
        BoardState b1 = null;
        //int v = -1001;
        ArrayList<BoardState> moves = board.allMoves(bS,colour);
        Collections.shuffle(moves);
        /*for (BoardState b2 : moves){
            int temp = Integer.max(v, minValue(b2, 1));
            if (v < temp){
                v = temp;
                b1 = b2;
            }
        }*/
        int a = - 3002;
        int be = 3002;
        for (BoardState b2 : moves){
            int temp = Integer.max(a, minValue2(b2, a, be, 1));
            if (a < temp){
                a = temp;
                b1 = b2;
            }
        }
        board.copyState(b1);
    }

    private int maxValue(BoardState bS, int depth){
        if (depth >= maxDepth){
            return evaluation(bS, depth);
        }
        Integer v = -1000;
        ArrayList<BoardState> moves = board.allMoves(bS,colour);
        for (BoardState b : moves){
            v = Integer.max(v, minValue(b, depth + 1));
        }
        return v;
    }

    private int maxValue2(BoardState bS, int a, int be, int depth){
        if (depth >= maxDepth){
            return evaluation(bS, depth);
        }
        ArrayList<BoardState> moves = board.allMoves(bS,colour);
        for (BoardState b : moves){
            a = Integer.max(a, minValue2(b, a, be, depth + 1));
            if (a > be){
                return a;
            }
        }
        if (moves.size() == 0){
            return evaluation(bS, depth);
        }
        return a;
    }

    private int minValue2(BoardState bS, int a, int be, int depth){
        if (depth >= maxDepth){
            return evaluation(bS, depth);
        }
        ArrayList<BoardState> moves = board.allMoves(bS,colour * -1);
        for (BoardState b : moves){
            be = Integer.min(be, maxValue2(b, a, be, depth + 1));
            if (be < a){
                return be;
            }
        }
        if (moves.size() == 0){
            return evaluation(bS, depth);
        }
        return be;
    }

    private int minValue(BoardState bS, int depth){
        if (depth >= maxDepth){
            return evaluation(bS, depth);
        }
        Integer v = 1000;
        ArrayList<BoardState> moves = board.allMoves(bS,colour * -1);
        for (BoardState b : moves){
            v = Integer.min(v, maxValue(b, depth + 1));

        }
        return v;
    }

    private int evaluation(BoardState b, int depth) {
        int goalTotal = 0;
        int avoidTotal = 0;
        if (b != null) {
            for (int x = 0; x < 8; x++) {
                for (int y = 0; y < 8; y++) {
                    Piece p = b.board[x][y];
                    if (p != null) {
                        if (p.isKing()) {
                            if (p.getColour() == colour) {
                                goalTotal = goalTotal + 5;
                            } else {
                                avoidTotal = avoidTotal - 5;
                            }
                        } else {
                            if (p.getColour() == colour) {
                                goalTotal = goalTotal + 2;
                            } else {
                                avoidTotal = avoidTotal - 2;
                            }
                        }

                        if (x == 0 || x == 7 || y == 0 || y == 7) {
                            if (p.getColour() == colour) {
                                if (!p.isKing())
                                    goalTotal++;
                            }
                        }

                    }
                }
            }
        } else {
            System.out.println("b is null");
            return -2000;
        }
        if (avoidTotal == 0){
            return 1000 - depth;
        }
        if (goalTotal == 0){
            return -1000 + depth;
        }
        return avoidTotal + goalTotal;
    }
}