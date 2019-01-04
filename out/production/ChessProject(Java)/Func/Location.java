package Func;

import java.awt.*;

/*
	Simple location class to keep track of locations
	 */
public class Location {
	private int x;
	private int y;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public Location() {
		x = 0;
		y = 0;
	}

	public Location(int x, int y) {
		this.x = x;
		this.y = y;
	}

	public Location(Dimension d) {
		this.x = d.width;
		this.y = d.height;
	}
}