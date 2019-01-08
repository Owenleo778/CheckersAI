import java.awt.*;

public class BlackPiece extends Piece {

    public BlackPiece(Point pos) {
        super(pos);
    }

    @Override
    public void checkKingPosition() {
        king = getPosition().y == 7;
    }

    @Override
    public Boolean correctDirection(int d) {
        return d != 0 && (d == Board.BLACK || king);
    }
}
