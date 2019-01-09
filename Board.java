import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

/**
 * A class that represents the board in checkers
 * @author Owen Salvage
 */
public class Board  extends JPanel implements MouseListener {
     
    // Top LEFT CORNER = 0,0
    public final static int BLACK = 1;
    public final static int WHITE = -1;
    public final static int AIPLAYER = BLACK;
    private Window window;
    BoardState board;
    private CheckerMinMax  ai = null;

    public Board(Window window){
        super();
        this.window = window;
        setBackground(Color.GRAY);
        setBorder(new LineBorder(Color.DARK_GRAY, 5));
        addMouseListener(this);
        setMinimumSize(new Dimension(8800,800));
        setPreferredSize(new Dimension(800,800));
        newGame();
    }

    public void newGame(){
        board = new BoardState();
        ai = new CheckerMinMax(this);
        System.out.println("Black's turn.");
    }

    public void play(){
        ai.takeTurn(board);
    }

    @Override
    public void paintComponent(Graphics g) {
        drawBoard(g);
        drawCheckers(g);
    }

    private void drawBoard(Graphics g){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if ((x + y) % 2 == 1) {
                    g.setColor(new Color(153, 76, 0));
                } else {
                    g.setColor(Color.WHITE);
                }
                int xSize = Math.round(x * getSize().width / 8);
                int ySize = Math.round(y * getSize().height / 8);
                g.fillRect(xSize , ySize , xSize + getSize().width / 8, ySize + getSize().height / 8);
            }
        }
    }

    private void drawCheckers(Graphics g){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                Piece p = board.board[x][y];
                if (p != null){
                    if (p.getColour() == BLACK){
                        g.setColor(Color.BLACK);
                    } else {
                        g.setColor(Color.WHITE);
                    }
                    drawChecker(g, p);
                    if (p.isKing()){
                        drawKingMarker(g,p);
                    }
                }
            }
        }
    }

    private void drawChecker(Graphics g, Piece p){
        Point pos = p.getPosition();
        int xSize = Math.round(pos.x * getSize().width / 8 + getSize().width / 80  );
        int ySize = Math.round(pos.y * getSize().height / 8 + getSize().height / 80);
        g.fillOval(xSize, ySize, getSize().width / 10, getSize().height / 10);
    }

    private void drawKingMarker(Graphics g, Piece p){
        Point pos = p.getPosition();
        g.setColor(Color.RED);
        int xSize = Math.round(pos.x * getSize().width / 8 + getSize().width / 20);
        int ySize = Math.round(pos.y * getSize().height / 8 + getSize().height / 20);
        g.fillOval(xSize, ySize, getSize().width / 40, getSize().height / 40);
    }

    private int getMouseX(int x){
        return x * 8 / getSize().width;
    }

    private int getMouseY(int y){
        return y * 8 / getSize().height;
    }

    /**
     * Checks if the coordinate is in range (between 0 and 7
     * @param pos the coordinate
     * @return true if it's in range, false otherwise.
     */
    private boolean inRange(int pos){
        return pos >= 0 && pos < 8;
    }

    /**
     * Returns true if the space specified is empty.
     * @param x the x position in question
     * @param y the y position in question
     * @return true if the space is empty, false otherwise.
     */
    private boolean isEmptySpace(int x, int y){
        return inRange(x) && inRange(y) && board.board[x][y] == null;
    }

    // TO BE USED BY AI
    public Boolean checkValidMove(int x1, int y1, int x2, int y2, int colour){
        if (inRange(x1) && inRange(x2) && inRange(y1) && inRange(y2)) {
            if (board.board[x1][y1] != null){
                if (Math.abs(x2 - x1) == 1 && colour == y2 - y1 && !jumpAvailable() && board.board[x2][y2] == null) { //move
                    //stuff
                    return true;
                } else if (Math.abs(x2 - x1) == 2 && colour == (y2 - y1) / 2 && board.board[x2][y2] == null) { //jump over piece
                    int midx = (x2 - x1) / 2;
                    int midy = (y2 - y1) / 2;
                    if (!isEmptySpace(x2 - midx, y2 - midy) && board.board[x2 - midx][y2 - midy].getColour() != colour) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void copyState(BoardState b){
        board = new BoardState(b);
        nextPlayerTurn();
        repaint();
    }

    // DOESN'T TAKE INTO ACCOUNT DOUBLE JUMPS
    public ArrayList<BoardState> allMoves(BoardState b, int colour){
        ArrayList<BoardState> boards = new ArrayList<>();
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++) {
                Piece p = b.board[x][y];
                if (p != null && p.getColour() == colour) {
                    for (int x2 = -2; x2 < 3; x2++){
                        if (x2 != 0){
                            int y2 = Math.abs(x2) * colour;
                            if (checkValidMove(x,y,x + x2,y + y2, colour)) {
                                boards.add(movePiece(b, x, y, x + x2, y + y2));
                            }
                            if (p.isKing()){
                                y2 = y2 * -1;
                                if (checkValidMove(x,y,x2,y2, colour)) {
                                    boards.add(movePiece(b, x, y, x + x2, y + y2));
                                }
                            }
                        }
                    }
                }
            }
        }
        return boards;
    }

    private BoardState movePiece(BoardState bS, int x1, int y1, int x2, int y2){
        BoardState b = new BoardState(bS);
        b.board[x2][y2] = b.board[x1][y1];
        b.board[x1][y1] = null;
        b.board[x2][y2].setPosition(new Point(x2,y2));
        if (Math.abs(x2 - x1) == 2){
            int xdiff = (x2 - x1) / 2;
            int ydif = (y2 - y1) / 2;
            b.board[x1 + xdiff][y1 + ydif] = null;
        }
        return b;
    }

    /**
     * Returns true if the current jump is valid.
     * @param p the piece in question.
     * @param midx the x coordinate of the middle tile.
     * @param midy the y coordinate of the middle tile.
     * @param x the final x position.
     * @param y the final y position.
     * @return true if the jump is valid, false otherwise.
     */
    private Boolean checkJump(Piece p, int midx, int midy, int x, int y){
        if (inRange(midy) && inRange(y) && inRange(midx) && inRange(x)){
            if (board.board[midx][midy] != null && board.board[midx][midy].getColour() != p.getColour() && isEmptySpace(x, y)){
                return true;
            }
        }
        return false;
    }

    /**
     * Moves the piece if it's a valid move
     * @param p the piece
     * @param x the x position
     * @param y the y position
     * @return true if the move was made, false otherwise
     */
    private Boolean moveChecker(Piece p, int x, int y){
        if (inRange(x) && inRange(y) && isEmptySpace(x,y)){
            Point pos = p.getPosition();
            if (Math.abs(x - pos.x) == 1 && p.correctDirection(y - pos.y)){
                if (!jumpAvailable()) {
                    board.board[pos.x][pos.y] = null;
                    board.board[x][y] = p;
                    p.setPosition(new Point(x, y));
                    checkForKing(p);
                    board.justJumped = false;
                    return true;
                } else {
                    System.out.println("Invalid move, must take the jump.");
                }
            } else if (Math.abs(x - pos.x) == 2 && p.correctDirection((y - pos.y) / 2)){
                int midx = (x - pos.x) / 2;
                int midy = (y - pos.y ) / 2;

                if (!isEmptySpace(x - midx, y - midy) && board.board[x - midx][y - midy].getColour() != p.getColour()){
                    board.board[x - midx][y - midy] = null;
                    board.board[pos.x][pos.y] = null;
                    board.board[x][y] = p;
                    p.setPosition(new Point(x,y));
                    checkForKing(p);
                    board.justJumped = true;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Returns true if a jump (over a piece) is available for the current player.
     * @return true if there's a jump, false if there isn't
     */
    private Boolean jumpAvailable(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if (!isEmptySpace(x,y)){
                    Piece p = board.board[x][y];
                    if (p.getColour() == board.playerTurn){
                        if (canPieceJump(p)) return true;
                    }
                }
            }
        }
        return false;
    }

    private Boolean moveAvailable(){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if (!isEmptySpace(x,y)){
                    Piece p = board.board[x][y];
                    if (p.getColour() == board.playerTurn){
                        if (canPieceMove(p)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * This checks if a piece can jump over ANOTHER piece, not just jump in general.
     * @param p the piece in question.
     * @return true if a jump exists, false otherwise.
     */
    private Boolean canPieceJump(Piece p){
       if (p.isKing()){
           return checkBlackJump(p) || checkWhiteJump(p);
       } else if (p.getColour() == BLACK){
           if (checkBlackJump(p)) return true;
       } else {
           if (checkWhiteJump(p)) return true;
       }
        return false;
    }

    private Boolean canPieceMove(Piece p){
        if (p.isKing()){
            return checkBlackMove(p) || checkWhiteMove(p);
        } else if (p.getColour() == BLACK){
            if (checkBlackMove(p)) return true;
        } else {
            if (checkWhiteMove(p)) return true;
        }
        return false;
    }

    private Boolean checkBlackJump(Piece p){
        Point pos = p.getPosition();
        return checkJump(p,pos.x - 1,pos.y + BLACK, pos.x - 2, pos.y + 2 * BLACK) ||
                checkJump(p,pos.x + 1,pos.y + BLACK, pos.x + 2, pos.y + 2 * BLACK);
    }

    private Boolean checkBlackMove(Piece p){
        Point pos = p.getPosition();
        return isEmptySpace(pos.x - 1, pos.y + BLACK) || isEmptySpace(pos.x + 1, pos.y + BLACK);
    }

    private Boolean checkWhiteJump(Piece p){
        Point pos = p.getPosition();
        return checkJump(p,pos.x - 1,pos.y + WHITE, pos.x - 2, pos.y + 2 * WHITE) ||
                checkJump(p,pos.x + 1,pos.y + WHITE, pos.x + 2, pos.y + 2 * WHITE);
    }
    private Boolean checkWhiteMove(Piece p){
        Point pos = p.getPosition();
        return isEmptySpace(pos.x - 1, pos.y + WHITE) || isEmptySpace(pos.x + 1, pos.y + WHITE);
    }

    /**
     * Sets to the next Players turn.
     */
    private void nextPlayerTurn(){
        if (board.playerTurn == BLACK){
            board.playerTurn = WHITE;
            System.out.println("White's turn.");
        } else {
            board.playerTurn = BLACK;
            System.out.println("Black's turn.");
        }
        if (jumpAvailable()) {
            System.out.println("There is a jump available.");
        }
        board.pieceToJump = null;

        if (board.playerTurn == AIPLAYER){
            ai.takeTurn(board);
        }
    }

    /**
     * Checks if the current piece can be a king, if so it makes it a king.
     * @param p the piece in question
     */
    private void checkForKing(Piece p){
        if (!p.isKing()){
            p.checkKingPosition();
        }
    }

    private void checkGameOver(){
        if (!jumpAvailable() && !moveAvailable()){
            String player;
            if (board.playerTurn == 1){
                player = "White";
            } else {
                player = "Black";
            }
            System.out.println(player + " wins!");
            System.exit(0);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int x = getMouseX(e.getX());
        int y = getMouseY(e.getY());


        // remove "playerTurn != AIPLAYER" to play 2 player
        if (!isEmptySpace(x,y) && board.playerTurn != AIPLAYER && board.board[x][y].getColour() == board.playerTurn) {
            if (board.pieceToJump == null){
                board.inHand = board.board[x][y];
            } else if (board.pieceToJump.x == x && board.pieceToJump.y == y)
                board.inHand = board.board[x][y];
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Boolean aiTurn = false;
        if (board.inHand != null) {
            int x = getMouseX(e.getX());
            int y = getMouseY(e.getY());
            if (moveChecker(board.inHand,x,y)) {
                if (board.justJumped && canPieceJump(board.board[x][y])) {
                    board.pieceToJump = board.board[x][y].getPosition();
                    System.out.println("Jump again.");
                } else {
                    aiTurn = true;
                }
            }
            board.inHand = null;
            repaint();
            checkGameOver();
            if (aiTurn){
                nextPlayerTurn();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
