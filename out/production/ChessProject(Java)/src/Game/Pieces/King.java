package Game.Pieces;

import java.awt.Point;

/*
The King piece
 */
public class King extends Piece {

	public King(PieceColor color, int x, int y) {
		super("King", x, y, color, 10);
	}

	@Override
	public char iden() { return 'K'; }

	@Override
	public boolean isValidMove(Point from, Point to) {
		int xDist = Math.abs(from.x - to.x);
		int yDist = Math.abs(from.y - to.y);
		return xDist == 1 || yDist == 1;			// either horizontal, vertical, or both
	}
}