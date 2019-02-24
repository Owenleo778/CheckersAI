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
    public final static int NOT_PLAYING = 0;
    public int aiPlayer;
    public int startPlayer;
    private BoardState board;
    private int aiDepth;
    private boolean singlePlayer;

    private Board(){
        super();
        setBackground(Color.GRAY);
        setBorder(new LineBorder(Color.DARK_GRAY, 5));
        addMouseListener(this);
        setMinimumSize(new Dimension(8800,800));
        setPreferredSize(new Dimension(800,800));
        singlePlayer = false;
        aiPlayer = NOT_PLAYING;
    }

    public Board(Boolean whiteStart){
        this();
        if (whiteStart){
            startPlayer = WHITE;
        } else {
            startPlayer = BLACK;
        }
    }

    public Board(Boolean whiteStart, Boolean playerWhite, int aiDepth){
        this(whiteStart);
        if (playerWhite){
            aiPlayer = BLACK;
        } else {
            aiPlayer = WHITE;
        }
        this.aiDepth = aiDepth;
        singlePlayer = true;
    }

    public void newGame(){
        board = new BoardState(startPlayer);
        if (startPlayer == BLACK){
            System.out.println("Black's turn.");
        } else {
            System.out.println("White's turn.");
        }
    }

    public void play(){
        newGame();
        if (aiPlayer == startPlayer){
            new Thread(new CheckerMinMax(this, startPlayer)).start();
        }
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
    private boolean isEmptySpace(BoardState b, int x, int y){
        return inRange(x) && inRange(y) && b.board[x][y] == null;
    }

    // TO BE USED BY AI
    public Boolean checkValidMove(BoardState b, int x1, int y1, int x2, int y2, int colour, boolean king){
        if (inRange(x1) && inRange(x2) && inRange(y1) && inRange(y2)) {
            if (b.board[x1][y1] != null){
                if (Math.abs(x2 - x1) == 1 && !jumpAvailable(b) && b.board[x2][y2] == null) { //move
                    if (colour == y2 - y1 || (king && Math.abs(y2 - y1) == 1)) {
                        //stuff
                        return true;
                    }
                } else if (Math.abs(x2 - x1) == 2 && b.board[x2][y2] == null) { //jump over piece
                    if (colour == (y2 - y1) / 2 || (king && Math.abs(y2 - y1) == 2)) {
                        int midx = (x2 - x1) / 2;
                        int midy = (y2 - y1) / 2;
                        if (!isEmptySpace(b,x2 - midx, y2 - midy) && b.board[x2 - midx][y2 - midy].getColour() != colour) {
                            return true;
                        }
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

    public BoardState getBoardState() {
        return board;
    }

    public ArrayList<BoardState> allMoves(BoardState b, int colour){
        ArrayList<BoardState> boards = new ArrayList<>();
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++) {
                boards.addAll(getMoves(b, colour, x, y,false));
            }
        }
        return boards;
    }

    private ArrayList<BoardState> getMoves(BoardState b, int colour, int x, int y, Boolean mustJump){
        ArrayList<BoardState> boards = new ArrayList<>();
        Piece p = b.board[x][y];
        if (p != null && p.getColour() == colour) {
            for (int x2 = -2; x2 < 3; x2++) {
                if (x2 != 0) {
                    int y2 = Math.abs(x2) * colour;
                    if (Math.abs(x2) == 2){
                        if (checkValidMove(b,x,y,x + x2,y + y2, colour, p.isKing())) {
                            boards.addAll(allJumps(b, x, y, x2, y2, colour));
                        }
                        if (p.isKing()){
                            y2 = y2 * -1;
                            if (checkValidMove(b,x,y,x + x2,y + y2, colour, p.isKing())) {
                                boards.addAll(allJumps(b, x, y,  x2, y2, colour));
                            }
                        }
                    } else if (!mustJump){
                        if (checkValidMove(b,x,y,x + x2,y + y2, colour, p.isKing())) {
                            BoardState bS = movePiece(b, x, y, x + x2, y + y2);
                            checkForKing(bS.board[x+x2][y+y2]);
                            boards.add(bS);
                        }
                        if (p.isKing()){
                            y2 = y2 * -1;
                            if (checkValidMove(b, x,y,x + x2,y + y2, colour, p.isKing())) {
                                BoardState bS = movePiece(b, x, y, x + x2, y + y2);
                                checkForKing(bS.board[x+x2][y+y2]);
                                boards.add(bS);
                            }
                        }
                    }
                }
            }
        }
        return boards;
    }

    private ArrayList<BoardState> allJumps(BoardState b, int x, int y, int x2, int y2, int colour){
        ArrayList<BoardState> jumps = new ArrayList<>();

        BoardState bS = movePiece(b, x, y, x + x2, y + y2);
        if (canPieceJump(bS, bS.board[x+x2][y+y2])){
            jumps.addAll(getMoves(bS, colour, x + x2, y + y2, true));
        } else {
            checkForKing(bS.board[x+x2][y+y2]);
            jumps.add(bS);
        }
        return jumps;
    }

    private BoardState movePiece(BoardState bS, int x1, int y1, int x2, int y2){
        BoardState b = new BoardState(bS);
        b.board[x2][y2] = b.board[x1][y1];
        b.board[x1][y1] = null;
        b.board[x2][y2].setPosition(new Point(x2,y2));
        b.drawCounter++;
        if (Math.abs(x2 - x1) == 2){
            int xdiff = (x2 - x1) / 2;
            int ydif = (y2 - y1) / 2;
            b.board[x1 + xdiff][y1 + ydif] = null;
            b.drawCounter = 0;
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
    private Boolean checkJump(BoardState b, Piece p, int midx, int midy, int x, int y){
        if (inRange(midy) && inRange(y) && inRange(midx) && inRange(x)){
            if (b.board[midx][midy] != null && b.board[midx][midy].getColour() != p.getColour() && isEmptySpace(b, x, y)){
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
    private Boolean moveChecker(BoardState b, Piece p, int x, int y){
        if (inRange(x) && inRange(y) && isEmptySpace(b, x,y)){
            Point pos = p.getPosition();
            if (Math.abs(x - pos.x) == 1 && p.correctDirection(y - pos.y)){
                if (!jumpAvailable(b)) {
                    b.board[pos.x][pos.y] = null;
                    b.board[x][y] = p;
                    p.setPosition(new Point(x, y));
                    b.justJumped = false;
                    b.drawCounter++;
                    return true;
                } else {
                    System.out.println("Invalid move, must take the jump.");
                }
            } else if (Math.abs(x - pos.x) == 2 && p.correctDirection((y - pos.y) / 2)){
                int midx = (x - pos.x) / 2;
                int midy = (y - pos.y ) / 2;

                if (!isEmptySpace(b,x - midx, y - midy) && b.board[x - midx][y - midy].getColour() != p.getColour()){
                    b.board[x - midx][y - midy] = null;
                    b.board[pos.x][pos.y] = null;
                    b.board[x][y] = p;
                    p.setPosition(new Point(x,y));
                    b.justJumped = true;
                    b.drawCounter = 0;
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
    private Boolean jumpAvailable(BoardState b){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if (!isEmptySpace(b, x,y)){
                    Piece p = b.board[x][y];
                    if (p.getColour() == b.playerTurn){
                        if (canPieceJump(b, p)) return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * Checks if the player can make a move
     * @param b the board state.
     * @return true if it can move, false otherwise.
     */
    private Boolean moveAvailable(BoardState b){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if (!isEmptySpace(b, x,y)){
                    Piece p = b.board[x][y];
                    if (p.getColour() == b.playerTurn){
                        if (canPieceMove(b, p)) return true;
                    }
                }
            }
        }
        return false;
    }


    // Can make the following methods more concise (made in this way due to earlier implementation of the Piece class)


    /**
     * This checks if a piece can jump over ANOTHER piece, not just jump in general.
     * @param p the piece in question.
     * @return true if a jump exists, false otherwise.
     */
    private Boolean canPieceJump(BoardState b, Piece p){
       return checkJump(b, p, p.isKing());
    }

    private Boolean canPieceMove(BoardState b, Piece p){
        return checkMove(b, p, p.isKing());
    }

    private Boolean checkJump(BoardState b, Piece p, boolean king){
        Point pos = p.getPosition();
        if (king){
            return checkJump(b, p,pos.x - 1,pos.y + p.getColour(), pos.x - 2, pos.y + 2 *  p.getColour()) ||
                    checkJump(b, p,pos.x + 1,pos.y +  p.getColour(), pos.x + 2, pos.y + 2 *  p.getColour()) ||
                    checkJump(b, p,pos.x - 1,pos.y + -1 * p.getColour(), pos.x - 2, pos.y + 2 *  -1 * p.getColour()) ||
                    checkJump(b, p,pos.x + 1,pos.y + -1 * p.getColour(), pos.x + 2, pos.y + 2 * -1 * p.getColour());
        }
        return checkJump(b, p,pos.x - 1,pos.y + p.getColour(), pos.x - 2, pos.y + 2 *  p.getColour()) ||
                checkJump(b, p,pos.x + 1,pos.y +  p.getColour(), pos.x + 2, pos.y + 2 *  p.getColour());
    }

    private Boolean checkMove(BoardState b, Piece p, boolean king){
        Point pos = p.getPosition();
        if (king){
            return isEmptySpace(b, pos.x - 1, pos.y +  p.getColour()) || isEmptySpace(b, pos.x + 1, pos.y +  p.getColour()) ||
                    isEmptySpace(b, pos.x - 1, pos.y + -1 * p.getColour()) || isEmptySpace(b, pos.x + 1, pos.y +  -1 * p.getColour());
        }
        return isEmptySpace(b, pos.x - 1, pos.y +  p.getColour()) || isEmptySpace(b, pos.x + 1, pos.y +  p.getColour());
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
        if (jumpAvailable(board)) {
            System.out.println("There is a jump available.");
        }
        board.pieceToJump = null;
        checkGameOver();

        if (singlePlayer) {
            if (board.playerTurn == aiPlayer) {
                new Thread(new CheckerMinMax(this, aiDepth)).start();
            } /*else if (board.playerTurn != 0) {
                new Thread(new CheckerMinMax(this, board.playerTurn)).start();
            }*/
        }
    }

    /**
     * Checks if the current piece can be a king, if so it makes it a king.
     * @param p the piece in question
     */
    private void checkForKing(Piece p){
        if (!p.isKing()){
            p.checkKingPosition();
            if (p.isKing()){
                board.drawCounter = 0;
            }
        }
    }

    private void checkGameOver(){
        if (!jumpAvailable(board) && !moveAvailable(board)){
            String player;
            if (board.playerTurn == 1){
                player = "White";
            } else {
                player = "Black";
            }
            board.playerTurn = 0;
            System.out.println(player + " wins!");
        } else if (board.drawCounter == 50){
            board.playerTurn = 0;
            System.out.println("50 turns of nothing happening, game ends in draw.");
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int x = getMouseX(e.getX());
        int y = getMouseY(e.getY());


        // remove "playerTurn != AIPLAYER" to play 2 player
        if (!isEmptySpace(board, x,y) && board.board[x][y].getColour() == board.playerTurn) {
            if (board.pieceToJump == null){
                board.inHand = board.board[x][y];
            } else if (board.pieceToJump.x == x && board.pieceToJump.y == y)
                board.inHand = board.board[x][y];
        }

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Piece p = null;
        Boolean moveMade = false;
        if (board.inHand != null) {
            int x = getMouseX(e.getX());
            int y = getMouseY(e.getY());
            if (moveChecker(board, board.inHand,x,y)) {
                if (board.justJumped && canPieceJump(board, board.board[x][y])) {
                    board.pieceToJump = board.board[x][y].getPosition();
                    System.out.println("Jump again.");
                } else {
                    p = board.inHand;
                    moveMade = true;
                }
                if (p != null){
                    checkForKing(p);
                }
            }
            board.inHand = null;
            repaint();

            if (moveMade){
                nextPlayerTurn();
            }
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
