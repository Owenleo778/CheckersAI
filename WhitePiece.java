import java.awt.*;

public class WhitePiece extends Piece{

    public WhitePiece(Point pos) {
        super(pos);
    }

    @Override
    public void checkKingPosition() {
        king = getPosition().y == 0;
    }

    @Override
    public Boolean correctDirection(int d) {
        return d != 0 && (d == Board.WHITE || king);
    }
}