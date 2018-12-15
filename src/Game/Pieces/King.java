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
		return Math.abs(from.x - to.x) == 1 && Math.abs(from.y - to.y) == 1;
	}
}