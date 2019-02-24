import java.awt.*;

/**
 * A class that stores the current state of the board.
 * @author Owen Salvage
 */
public class BoardState {

    public Piece[][] board;
    public Piece inHand;
    public int playerTurn;
    public Point pieceToJump;
    public boolean justJumped;
    public int drawCounter;

    public BoardState(int startPlayer) {
        inHand = null;
        playerTurn = startPlayer;
        pieceToJump = null;
        justJumped = false;
        drawCounter = 0;
        placePieces();
    }

    /**
     * Sets the parameters to the same as the one passed in
     * @param b
     */
    public BoardState(BoardState b){
        board = new Piece[8][8];
        inHand = null;

        playerTurn = b.playerTurn;
        drawCounter = b.drawCounter;

        if (b.pieceToJump != null) {
            pieceToJump = new Point(b.pieceToJump.x, pieceToJump.y);
        } else {
            pieceToJump = null;
        }
        justJumped = b.justJumped;
        for (int x = 0; x < 8; x++){
            for (int y = 0; y <8; y++){
                if (b.board[x][y] != null){
                    Piece p = new Piece(b.board[x][y]);
                    board[x][y] = p;
                }
            }
        }
    }

    private void placePieces(){
        board = new Piece[8][8];
        placeBlacks();
        placeWhites();
    }

    private void placeBlacks(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 3; y++){
                if ((x + y) % 2 == 1){
                    Piece p = new Piece(new Point(x,y), Board.BLACK);
                    board[x][y] = p;
                }
            }
        }
    }

    private void placeWhites(){
        for (int x = 0; x < 8; x++){
            for (int y = 5; y < 8; y++){
                if ((x + y) % 2 == 1){
                    Piece p = new Piece(new Point(x,y), Board.WHITE);
                    board[x][y] = p;
                }
            }
        }
    }
}