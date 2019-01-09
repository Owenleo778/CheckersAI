import java.awt.*;

public class BoardState {

    public Piece[][] board;
    private Piece inHand;
    private int playerTurn;
    private Point pieceToJump;
    private boolean justJumped;

    public BoardState(){
        inHand = null;
        playerTurn = Board.BLACK;
        pieceToJump = null;
        justJumped = false;
        placePieces();
    }

    public BoardState(BoardState b){
        if (b.inHand == null){
            inHand = null;
        } else {
            inHand = new Piece(b.inHand);
        }
        playerTurn = b.playerTurn;
        pieceToJump = new Point(b.pieceToJump.x, pieceToJump.y);
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
