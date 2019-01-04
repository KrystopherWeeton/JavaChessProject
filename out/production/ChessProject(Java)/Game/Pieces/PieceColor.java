package Game.Pieces;

public enum PieceColor{
	dark ("DARK"),
	light ("LIGHT");


	final private String str;
	PieceColor(String str) {
		this.str = str;
	}

	public String string() { return str; }
}