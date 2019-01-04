package Game.Pieces;

import java.awt.Point;

/*
The Knight
 */
public class Knight extends Piece{

	public Knight(PieceColor color, int x, int y) {
		super("Knight", x, y, color, 3);
	}

	@Override
	public char iden() { return 'N'; }

	@Override
	public boolean isValidMove(Point from, Point to) {
		int xDiff = Math.abs(from.x - to.x);
		int yDiff = Math.abs(from.y - to.y);
		return (xDiff == 1 && yDiff == 2) || (xDiff == 2 && yDiff == 1);
	}

}