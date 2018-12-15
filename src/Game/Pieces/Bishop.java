package Game.Pieces;

import java.awt.Point;

/*
The Bishop
 */
public class Bishop extends Piece{

	public Bishop(PieceColor color, int x, int y) {
		super("Bishop", x, y, color, 3);
	}

	@Override
	public char iden() { return 'B'; }

	@Override
	public boolean isValidMove(Point from, Point to) {
		return Math.abs(from.x - from.y) == Math.abs(to.x - to.y) || (from.x + from.y == to.x + to.y );
	}
}