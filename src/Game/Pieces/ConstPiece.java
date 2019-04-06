package Game.Pieces;

import java.awt.Point;

public class ConstPiece {

	Piece p;

	public ConstPiece(Piece p) {
		this.p = p;
	}

	public PieceColor getColot() {
		return p.getColor();
	}

	public boolean colorMatches(PieceColor color){
		return p.colorMatches(color);
	}

	public char iden() {
		return p.iden();
	}

	public Point getPoint() {
		return (Point)p.getPoint().clone();
	}

	public String toString() {
		return p.toString();
	}

	public int getValue() {
		return p.points;
	}

}
