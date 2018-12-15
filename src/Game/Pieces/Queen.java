package Game.Pieces;

import java.awt.Point;

/*
The Queen
 */
public class Queen extends Piece {

	public Queen(PieceColor color, int x, int y) {
		super("Queen", x, y, color,9);
	}

	@Override
	public char iden() { return 'Q'; }

	@Override
	public boolean isValidMove(Point from, Point to) {
		return Math.abs(from.x - from.y) == Math.abs(to.x - to.y)
				|| (from.x + from.y == to.x + to.y ) || from.x == to.x || from.y == to.y;
	}

}