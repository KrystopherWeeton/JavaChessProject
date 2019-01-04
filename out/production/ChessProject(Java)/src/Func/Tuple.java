package Func;

public class Tuple<X, Y> {

	final public X x;
	final public Y y;

	public Tuple(X x, Y y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public String toString() {
		return "<" + x + ", " + y + ">";
	}
}