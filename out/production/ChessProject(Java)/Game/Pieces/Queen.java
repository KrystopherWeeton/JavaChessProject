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
		int xDiff = Math.abs(from.x - to.x);
		int yDiff = Math.abs(from.y - to.y);
		return xDiff == yDiff || from.x == to.x || from.y == to.y;
	}

}