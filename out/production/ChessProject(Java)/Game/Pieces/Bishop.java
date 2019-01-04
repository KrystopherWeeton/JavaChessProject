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
		int distX = Math.abs(from.x - to.x);		// calculates the horizontal distance
		int distY = Math.abs(from.y - to.y);		// calculates vertical distance
		return distX == distY && !from.equals(to);	// makes sure distances are equal and we are moving
	}
}