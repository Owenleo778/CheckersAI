import java.awt.*;

// Refactoring Board -> include all data about board in boardstate
// THis is to make easier for ai to work without fucking everything up




public class BoardState {

    public Piece[][] board;
    public Piece inHand;
    public int playerTurn;
    public Point pieceToJump;
    public boolean justJumped;

    public BoardState(){
        inHand = null;
        playerTurn = Board.BLACK;
        pieceToJump = null;
        justJumped = false;
        placePieces();
    }

    public BoardState(BoardState b){
        board = new Piece[8][8];
        inHand = null;

        playerTurn = b.playerTurn;

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
