import java.awt.Point;

/**
 * A class that represents a piece (a man)
 * @author Owen Salvage
 */
public abstract class Piece {

    /*
    private int colour;
      if (colour == Board.BLACK || colour == Board.WHITE){
        this.colour = colour;
    } else {
        throw new IndexOutOfBoundsException("Number entered not -1,1");
    }
    */
    private Point position;
    protected Boolean king;

    public Piece(Point pos){
        if (pos != null){
            position = pos;
        } else {
            throw new NullPointerException("null was entered for position.");
        }
        king = false;
    }

    public Point getPosition(){
        return position;
    }

    public void setPosition(Point pos){
        position = pos;
    }

    public Boolean isKing(){
        return king;
    }

    /**
     * A method to be called when the piece is moved, checks if it should be made a king piece.
     */
    public abstract void checkKingPosition();

    /**
     * Used to check if the piece can be moved up / down (dependent on colour.
     * @param d either 1 or -1, representing down or up the board.
     * @return true if it can, false otherwise.
     */
    public abstract Boolean correctDirection(int d);

}