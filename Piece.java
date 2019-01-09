import java.awt.Point;

/**
 * A class that represents a piece
 * @author Owen Salvage
 */
public class Piece {

    private Point position;
    private int colour;
    private boolean king;

    public Piece(Point pos, int colour){
        if (pos != null){
            position = pos;
        } else {
            throw new NullPointerException("null was entered for position.");
        }
        if (colour == Board.BLACK || colour == Board.WHITE){
            this.colour = colour;
        } else {
            throw new IndexOutOfBoundsException("Number entered not -1,1");
        }
        king = false;
    }

    public Piece(Piece p){
        position = new Point(p.getPosition().x, p.getPosition().y);
        this.colour = p.getColour();
        king = p.isKing();
    }

    /**
     * Returns the current position of the piece.
     * @return
     */
    public Point getPosition(){
        return position;
    }

    /**
     * Sets the current position of the piece.
     * @param pos the new position.
     */
    public void setPosition(Point pos){
        position = pos;
    }

    /**
     * Returns the colour (-1 or 1) of the piece (White or Black respectively).
     * @return it's colour.
     */
    public int getColour(){
        return colour;
    }

    /**
     * Returns whether this piece is a king or not.
     * @return if this piece is a king.
     */
    public Boolean isKing(){
        return king;
    }

    /**
     * A method to be called when the piece is moved, checks if it should be made a king piece.
     */
    public void checkKingPosition() {
        if (getColour() == Board.BLACK) {
            king = getPosition().y == 7;
        } else if (getColour() == Board.WHITE){
            king = getPosition().y == 0;
        }
    }

    /**
     * Used to check if the piece can be moved up / down (dependent on colour.
     * @param d either 1 or -1, representing down or up the board.
     * @return true if it can, false otherwise.
     */
    public Boolean correctDirection(int d) {
        return Math.abs(d) == 1 && (d == getColour() || king);
    }

}