package Game.Pieces;

import javax.imageio.*;
import java.awt.*;
import java.net.*;
import java.util.HashMap;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.util.logging.*;


/*
The base class for all pieces in the game
 */
public abstract class Piece {


	/*
	Static variables - mainly used for the sprite initialization process
	 */

	private static boolean created = false;
	private static HashMap<String, ImageIcon> sprites = new HashMap<>();

	final private static String[] SPRITE_ORDER = {"King", "Queen", "Rook", "Knight", "Bishop", "Pawn"};
	final private static String SPRITE_PATH = "PieceImages/Pieces.png";
	final private static int SPRITE_LENGTH = 64;	// in pixels

	final private static Logger logger = Logger.getLogger("Logger");

	/*
	Non-static variables
	 */

	final private ImageIcon image;

	final private PieceColor color;

	private int x;
	private int y;

	final public int points;

	abstract public char iden();

	abstract public boolean isValidMove(Point from, Point to);

	public Piece(String name, PieceColor color, int points) {
		this(name, 0, 0, color, points);
	}

	public Piece(String name, int x, int y, PieceColor color, int points) {
		if (!created)    // First time a piece is being created, load up the sprites
			Piece.loadSprites();

		this.x = x;
		this.y = y;
		this.points = points;
		this.color = color;

		// Sets the image
		String key = color.string() + name;
		if (sprites.containsKey(key)) {
			this.image = sprites.get(key);
		} else {
			System.err.println("Could not load image for one of the pieces");
			this.image = null;
		}
	}

	public Point getPoint() {
		return new Point(x, y);
	}

	/*
	Sets the location fields for the piece
	 */
	public void setLocation(int x, int y) {
		this.x = x;
		this.y = y;
	}

	/*
	Returns the x and y coordinates of the pieces location
	 */
	public int getX() { return x; }
	public int getY() { return y; }

	/*
	Returns the image for this piece
	 */
	public ImageIcon getImage() {
		return image;
	}

	/*
	Returns the color of this
	 */
	public PieceColor getColor() {
		return color;
	}

	/*
	Returns a boolean representing whether or not the colors of the pieces match
	 */
	public boolean colorMatches(Piece p) {
		return p != null && this.color == p.color;
	}

	public boolean colorMatches(PieceColor color) { return this.color == color; }

	/*
	Loads the sprites for all the pieces and puts them into the sprites map
	 */
	private static void loadSprites() {
			logger.config("Entered loadSprites");
			try {

				// Read the sprite file
				File file = new File(SPRITE_PATH);
				BufferedImage bi = ImageIO.read(file);

				// Take out the individual sprites
				for (int y = 0; y < 2; y++) {
					String color = (y == 0) ? PieceColor.dark.string() : PieceColor.light.string();
					for (int x = 0; x < 6; x++) {    // First dark pieces and then light
						sprites.put(color + SPRITE_ORDER[x], readSprite(bi, x, y));
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
	}

	private static ImageIcon readSprite(BufferedImage bi, int x, int y) {
		return new ImageIcon(bi.getSubimage(x * Piece.SPRITE_LENGTH,
				y * Piece.SPRITE_LENGTH, Piece.SPRITE_LENGTH, Piece.SPRITE_LENGTH));
	}

	@Override
	public String toString() {
		return Character.toString(iden());
	}
}