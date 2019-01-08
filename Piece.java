import java.awt.Point;

/**
 * A class that represents a piece (a man)
 * @author Owen Salvage
 */
public class Piece {

    private Point position;
    private int colour;
    protected Boolean king;

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

    public Point getPosition(){
        return position;
    }

    public void setPosition(Point pos){
        position = pos;
    }

    public int getColour(){
        return colour;
    }

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