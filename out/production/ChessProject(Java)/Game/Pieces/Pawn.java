package Game.Pieces;

import java.awt.Point;

public class Pawn extends Piece {

	public Pawn(PieceColor color, int x, int y) {
		super("Pawn", x, y, color, 1);
	}

	@Override
	public char iden() { return 'P'; }

	@Override
	public boolean isValidMove(Point from, Point to) {
		int xDiff = Math.abs(to.x - from.x);
		int yDiff = to.y - from.y;					// keeping track of sign to ensure right direction
		if (colorMatches(PieceColor.light))
			return (xDiff == 0 && (yDiff == 1 || yDiff == 2)) || (xDiff == 1 && yDiff == 1);
		return (xDiff == 0 && (yDiff == -1 || yDiff == -2)) || (xDiff == 1 && yDiff == -1);
	}

}
