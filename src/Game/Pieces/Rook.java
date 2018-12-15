package Game.Pieces;

import java.awt.Point;

/*
The Rook
 */
public class Rook extends Piece {

	public Rook(PieceColor color, int x, int y) {
		super("Rook", x, y, color, 5);
	}

	@Override
	public char iden() { return 'R'; }

	@Override
	public boolean isValidMove(Point from, Point to) {
		return from.x == to.x || from.y == to.y;
	}


}