import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;


// Fix kings being able to jump lots of spaces





/**
 * A class that represents the board in checkers
 * @author Owen Salvage
 */
public class Board  extends JPanel implements MouseListener {


    // Top LEFT CORNER = 0,0
    public final static int BLACK = 1;
    public final static int WHITE = -1;
    public Piece[][] board;
    private Window window;
    private Piece inHand;

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
        inHand = null;
        placePieces();
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
                    Piece p = new BlackPiece(new Point(x,y));
                    board[x][y] = p;
                }
            }
        }
    }

    private void placeWhites(){
        for (int x = 0; x < 8; x++){
            for (int y = 5; y < 8; y++){
                if ((x + y) % 2 == 1){
                    Piece p = new WhitePiece(new Point(x,y));
                    board[x][y] = p;
                }
            }
        }
    }

    public void play(){

    }

    @Override
    public void paintComponent(Graphics g) {
        drawBoard(g);
        drawCheckers(g);
    }

    private void drawBoard(Graphics g){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                if ((x+y) % 2 == 1)
                    g.setColor(new Color(153, 76, 0));
                else
                    g.setColor(Color.WHITE);

                int xSize = Math.round(x * getSize().width / 8);
                int ySize = Math.round(y * getSize().height / 8);

                g.fillRect(xSize , ySize , xSize + getSize().width / 8, ySize + getSize().height / 8);
            }
        }
    }

    private void drawCheckers(Graphics g){
        for (int x = 0; x < 8; x++){
            for (int y = 0; y < 8; y++){
                Piece p = board[x][y];
                if (p != null){
                    if (p.getClass() == BlackPiece.class){
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

    private boolean inRange(int pos){
        return pos >= 0 && pos < 8;
    }

    /**
     * Moves the piece if it's a valid move
     * @param p the piece
     * @param x the x position
     * @param y the y position
     * @return true if it jumped over a piece, false otherwise
     */
    private Boolean moveChecker(Piece p, int x, int y){
        if (inRange(x) && inRange(y) && board[x][y] == null){
            Point pos = p.getPosition();
            if (Math.abs(x - pos.x) == 1 && p.correctDirection(y - pos.y)){
                board[pos.x][pos.y] = null;
                board[x][y] = p;
                p.setPosition(new Point(x,y));
                checkForKing(p);
                return false;
            } else if (Math.abs(x - pos.x) == 2 && p.correctDirection((y - pos.y) / 2)){
                int midx = (x - pos.x) / 2;
                int midy = (y - pos.y ) / 2;

                if (board[x - midx][y - midy] != null && board[x - midx][y - midy].getClass() != p.getClass()){
                    board[x - midx][y - midy] = null;
                    board[pos.x][pos.y] = null;
                    board[x][y] = p;
                    p.setPosition(new Point(x,y));
                    checkForKing(p);
                    return true;
                }
            }
        }
        return false;
    }

    private void checkForKing(Piece p){
        if (!p.isKing()){
            p.checkKingPosition();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {}

    @Override
    public void mousePressed(MouseEvent e) {
        int x = getMouseX(e.getX());
        int y = getMouseY(e.getY());
        inHand = board[x][y];
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (inHand != null) {
            int x = getMouseX(e.getX());
            int y = getMouseY(e.getY());
            moveChecker(inHand,x,y);
            inHand = null;
            repaint();
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}

    @Override
    public void mouseExited(MouseEvent e) {}
}
