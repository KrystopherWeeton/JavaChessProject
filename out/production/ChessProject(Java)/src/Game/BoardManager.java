package Game;

import Game.Exceptions.*;
import Game.Pieces.*;
import Func.Consts;

import java.util.*;
import java.awt.Point;
import java.util.logging.*;

import Func.Function;
import Func.Tuple;

/*
Handles the logic of the game, and stores all values
 */
public class BoardManager {

	/*
	----------------------------
	Class Fields
	----------------------------
	 */

	// Search funcs which look for pieces of a specific color according to move patterns
	private final SearchFunc straightPieces =
			new SearchFunc(new char[]{'R', 'Q'}, true, PieceColor.light);

	private final SearchFunc diagPieces =
			new SearchFunc(new char[]{'B', 'Q'}, true, PieceColor.light);

	private final SearchFunc knightPiece =
			new SearchFunc(new char[]{'N'}, false, PieceColor.light);

	private final SearchFunc pawnPiece =
			new SearchFunc(new char[]{'P'}, false, PieceColor.light);

	private final SearchFunc kingPiece =
			new SearchFunc(new char[]{'K'}, false, PieceColor.light);

	// a matrix to track the board
	private Piece[][] board = new Piece[ 8 ][ 8 ];

	// the point trackers
	private int lightPoints = 0;
	private int darkPoints = 0;

	// variable accessed within Inner Class (in implementation of func), so added as field for that purpose
	private boolean isLegal = false;

	// keeps track of whether the last move was a double forward by a pawn, along with the result location
	private Point prevMoveDoublePawn = null;

	// trackers to be used to keep track of king locations
	// should not be used to illegally modify the king information
	private King lightKing;
	private King darkKing;

	private static final Logger logger = Logger.getLogger("BoardManager");
	private static final boolean LOGGING = Consts.LOGGING;

	/*
	----------------------------
	Constructors and Generators
	----------------------------
	 */

	public BoardManager() {
		generatePieces();
	}

	/*
	Generates the pieces to be used for the game, putting them in the standard position for a Chess game
	 */
	private void generatePieces() {

		if (LOGGING)
			logger.config("Entering generatePieces");

		// Generate the pawns for both colors
		int lightY = 1;
		int darkY = 6;
		for (int x = 0; x < 8; x++) {
			setPiece(new Pawn(PieceColor.dark, x, darkY), x, darkY);
			setPiece(new Pawn(PieceColor.light, x, lightY), x, lightY);
		}

		// generate the pieces for the light color
		setPiece(new Rook(PieceColor.light, 0, 0), 0, 0);
		setPiece(new Knight(PieceColor.light, 1, 0), 1, 0);
		setPiece(new Bishop(PieceColor.light, 2, 0), 2, 0);
		setPiece(new Queen(PieceColor.light, 3, 0), 3, 0);
		lightKing = new King(PieceColor.light, 4, 0);
		setPiece(lightKing, 4, 0);
		setPiece(new Bishop(PieceColor.light, 5, 0), 5, 0);
		setPiece(new Knight(PieceColor.light, 6, 0), 6, 0);
		setPiece(new Rook(PieceColor.light, 7, 0), 7, 0);

		// generates the pieces for the dark color
		setPiece(new Rook(PieceColor.dark, 0, 7), 0, 7);
		setPiece(new Knight(PieceColor.dark, 1, 7), 1, 7);
		setPiece(new Bishop(PieceColor.dark, 2, 7), 2, 7);
		setPiece(new Queen(PieceColor.dark, 3, 7), 3, 7);
		darkKing = new King(PieceColor.dark, 4, 7);
		setPiece(darkKing, 4, 7);
		setPiece(new Bishop(PieceColor.dark, 5, 7), 5, 7);
		setPiece(new Knight(PieceColor.dark, 6, 7), 6, 7);
		setPiece(new Rook(PieceColor.dark, 7, 7), 7, 7);

	}

	/*
	----------------------------
	Basic Access Functionality
	----------------------------
	 */

	public String getPieceList(PieceColor color) {
		StringBuilder sb = new StringBuilder();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {
				if (at(x, y) != null)
					sb.append(" " + at(x, y).iden());
			}
		}
		return sb.toString();
	}

	// Accessor for lightPoints
	public int getLightPoints() {
		return lightPoints;
	}

	// Accessor for darkPoints
	public int getDarkPoints() {
		return darkPoints;
	}

	/*
	Determines if a given point is valid
	@param p - the point to check
	@return true if the point lies on the board and false otherwise
	 */
	public static boolean isValid(Point p) {
		return !(p == null || p.x < 0 || p.x > 7 || p.y < 0 || p.y > 7);
	}

	// Overload version of isValid with a point
	public static boolean isValid(int x, int y) { return isValid(new Point(x, y)); }

	/*
	Moves the piece to the given coordinates, setting old position to null. If null is passed, sets location
	to null.
	@param p - the piece to be set
	@param x,y - the location for the piece to go
	 */
	private void setLocation(Piece p, int x, int y) {
		if (p != null) {
			setPiece(null, p.getX(), p.getY());      // sets old location to null
			p.setLocation(x, y);                        // sets the new location of the piece
		}
		setPiece(p, x, y);								// sets the piece at the new location
	}

	/*
	Sets the piece at the position
	Note: This is private for a reason, very dangerous as it can de-sync piece
	locations and their positions in the board so be careful with it
	 */
	private void setPiece(Piece p, int x, int y) {
		board[x][y] = p;
	}

	/*
	Checks whether the piece at the given location is of the color specified
	@param color - the color the piece should be
	@param location - the location to be checked
	@return - true if the piece at location is of the color specified, false if it is null or a diff. color
	 */
	public boolean colorMatches(Point location, PieceColor color) {
		Piece p = at(location);
		return p != null && p.colorMatches(color);
	}

	/*
	Clones the board, and casts is back to Piece[][] so it can be passed to other functions without
	the danger of being changed
	 */
	public Piece[][] cloneBoard() {
		if (LOGGING)
			logger.config("Entering cloneBoard");
		return (Piece[][])board.clone();
	}

	/*
	Finds the piece at the given location. If the point is null or invalid, null is returned.
	@param Point - the point to be searched for
	@return - the piece at that point, null if no piece is found
	 */
	private Piece at(Point p) { return at(p.x, p.y); }

	private Piece at(int x, int y) {
		if (!isValid(x, y))
			return null;
		return board[x][y];
	}

	/*
	Returns the location of the king specificed
	@param color - the color fo the king to be searched for
	@return - the location of the king in the board
	 */
	public Point findKing(PieceColor color) {
		return (color == PieceColor.light) ? lightKing.getPoint() : darkKing.getPoint();
	}

	/*
	Determines if the given piece is at the starting row. Returns false if the piece passed is null
	@param p - the piece to check
	@return - true if the piece is at the starting row, false otherwise
	 */
	public boolean atStartingRow(Piece p) {
		if (p == null)
			return false;
		return ( (p.colorMatches(PieceColor.light) && p.getY() == 1) ||
				(p.colorMatches(PieceColor.dark) && p.getY() == 6) );
	}

	/*
	-------------------------------
	Turn Logic / Boolean Methods
	-------------------------------
	 */

	/*
	Checks for the validity and legality of the move and then performs it
	@param from - the location to move from
	@param to - the location to move to
	@return - a boolean representing whether or not the game is over
	 */
	public boolean performMove(Point from, Point to) {
		if (LOGGING)
			logger.info("Entering performMove from " + from + " to " + to);
		// determine validity and legality
		if (!isValid(from) || !isValid(to))
			throw new BoardIndexException("Attempt to perform move with one or more illegal" +
					"points from: " + from + " to: " + to);
		else if (!isValidMove(from, to))
			throw new InvalidMoveException("Attempt to perform an invalid move.");
		else if (!isLegalMove(from, to))
			throw new IllegalMoveException("Attempt to perform an illegal move.");


		// determine the two pieces that are being dealt with
		Piece movingPiece = at(from);
		Piece deadPiece = at(to);

		// clean the corpse for the dead one
		if (deadPiece != null) {
			if (deadPiece.getColor() == PieceColor.light)	// updates the point total
				darkPoints += deadPiece.points;
			else
				lightPoints += deadPiece.points;
		}

		boolean isEnPassant = isEnPassant(from, to);

		// check and update our doublePawnMove to keep track of the possibility of en-passant
		if (at(from) != null && at(from).iden() == 'P' && to.x == from.x && Math.abs(to.y - from.y) == 2)
			prevMoveDoublePawn = (Point)to.clone();		// clone the point just to be safe
		else
			prevMoveDoublePawn = null;

		// move the pieces on the board
		setLocation(movingPiece, to.x, to.y);	// set new location to new piece (taken piece is g.c.)
		setLocation(null, from.x, from.y);	// set old location to null

		// handle edge case for taking in an en passant
		if (isEnPassant) {
			if (movingPiece.colorMatches(PieceColor.light))
				lightPoints++;
			else
				darkPoints++;
			setLocation(null, to.x, (movingPiece.colorMatches(PieceColor.light) ? (to.y - 1) : (to.y + 1) ));
		}
		if (LOGGING) {
			logger.config("Light: " + getPieceList(PieceColor.light));
			logger.config("Dark: " + getPieceList(PieceColor.dark));
		}

		// checks if the game is over
		return isGameOver(movingPiece.getColor());
	}

	/*
	Determines whether or not a move is actually a valid one in the movement of the pieces
	i.e. the direction matches, and we don't need to jump pieces, and for pawns the diagonals match
	@param from - the location to move from
	@param to - the location to move to
	@return - true if the move is a valid one and false if the move is not a valid one
	 */
	public boolean isValidMove(Point from, Point to) {
		// Need to check (a) within valid lines, (b) is not going on top of a piece of the same color,
		// (c) nothing is blocking it (excluding knight movement) along with special considerations
		// pawns, castling, etc.

		// (a) within valid lines
		Piece p = at(from);
		Piece taken = at(to);
		if (p == null || !p.isValidMove(from, to))		// checks for degenerate case and that move lines up
			return false;


		// (b) not going to end up on top of a piece of the same color
		if (taken != null && taken.colorMatches(p))
			return false;

		// (c) nothing is blocking it (excluding knight movement)

		// f is a function that will keep checking until we hit something and update isLegal to check
		// if we have actually made it to the point we are trying to get to
		Function f = new Function() {
			@Override
			public boolean run(int x, int y) {
				Piece p = at(x, y);				// get the piece
				if (x == to.x && y == to.y) {	// if we hit the point we are trying to get, we are done
					isLegal = true;	// move is legal (we already guaranteed not same color)
					return false;	// no need to continue
				}
				return p == null;	// if we hit a piece, then we are done in this direction
			}
		};

		switch (p.iden()) {		// to determine whether or not the move is valid given specific considerations
			case 'K':	// must check whether it puts the king in check
				boolean ret = kingCanTake(from, to);
				return kingCanTake(from, to);
			case 'Q':	// check diagonals and lines
				isLegal = false;
				checkDiagonals(from, f);
				if (isLegal) return true;
				checkLines(from, f);
				return isLegal;
			case 'B':	// check diagonals
				isLegal = false;
				checkDiagonals(from, f);
				return isLegal;
			case 'N':	// don't need to do anything (a) checks hits and (b) checks right color / null
				// this case is covered in initial valid move check of piece
				return true;
			case 'R':	// check lines
				isLegal = false;
				checkLines(from, f);
				return isLegal;
			case 'P':
				return isValidPawnMove(from, to);
		}
		System.err.println("Unidentified piece was checked for a valid move.");
		return true;
	}

	public boolean isValidPawnMove(Point from, Point to) {

		// case 1: we are attempting to do a diagonal movement (better be taking something)
		if (to.x != from.x) {
			if (at(to) != null)	// standard diagonal movement
				return true;
			return isEnPassant(from, to);
		} else if (Math.abs(to.y - from.y) == 1) { // case 2: moving forward one
			return at(to) == null;
		} else if (Math.abs(to.y - from.y) == 2) { // case 3: move forward two
			return atStartingRow(at(from));
		}

		System.err.println("Attempt to check valid pawn move, without a call to pawn's valid move");
		return false;
	}

	/*
	Determines whether the move given violates the rules of Chess in any way (i.e. king in check,
	puts king in check, or pieces blocking, etc.)
	@param from - the point moving from
	@param to - the point to be moved to
	@return - true if the move is legal, and false if the move is not legal
	 */
	public boolean isLegalMove(Point from, Point to) {
		// Need to check (a) whether this move leaves the kin in check or puts the king in check

		if (at(from) == null)
			return false;		// you can't move nothing

		PieceColor color = at(from).getColor();	// gets the color of the moving piece

		return !testMove(from, to, new Function() {
			@Override
			public boolean run(int x, int y) {
				return kingInCheck(color);
			}
		});
	}

	/*
	Determines if the move is an enPassant
	@param from - the origin of the move
	@param to - the destination of the move
	@return - true if the move is an enPassant, false otherwise
	 */
	private boolean isEnPassant(Point from, Point to) {
		if (at(from) == null || at(from).iden() != 'P')		// degenerate case of not a pawn
			return false;

		if (isValid(prevMoveDoublePawn) && prevMoveDoublePawn.x == to.x		// if the prev move was
				&& Math.abs(prevMoveDoublePawn.y - to.y) == 1) {			// a double pawn and loc. match
			// we have an en-passant
			return ( at(prevMoveDoublePawn) != null && at(prevMoveDoublePawn).iden() == 'P');
			// might be able to replace this with return true; however, doesn't hurt to check.
		}
		return false;
	}

	/*
	Determines if the game has been won, returning the appropriate boolean
	@param color - the color of the player to check if they are winning
	@return - a boolean representing whether or not the game is over
	 */
	public boolean isGameOver(PieceColor moved) {
		PieceColor moveColor = (moved == PieceColor.dark) ? (PieceColor.light) : PieceColor.dark;

		ArrayList<Tuple<Point, Point>> moves = findAllMoves(moveColor);
		if (LOGGING)
			logger.config("Checking isGameOver with moves, " + moves);

		for (Tuple<Point, Point> t : moves) {
			if ( at(t.x) != null && at(t.x).colorMatches(moveColor) &&
					isValidMove(t.x, t.y) && isLegalMove(t.x, t.y)) {
				return false;
			}
		}

		return true;

	}


	/*
	Checks whether the king can make the move specified
	@param origLocation - the original location of the king (which must contain a king)
	@param location - the location to move the king to
	@return - a boolean which is true if the king can take the given position and false if the king
	cant take the given position
	 */
	public boolean kingCanTake(Point orig, Point location) {
		if (at(orig) == null || at(orig).iden() != 'K') return false;	// check degenerate case
		Function isKingInCheck = new Function() {
			@Override
			public boolean run(int x, int y) {
				return canBeTaken(at(orig));
			}
		};
		if( testMove(orig, location, isKingInCheck)) {
			return false;
		}

		// Check if we are moving next to another king
		return true;
	}

	/*
	Checks whether the given king is currently in check
	@param color - the color of the king to be checked
	@return - true if the king is in check and false if the king is not in check
	 */
	public boolean kingInCheck(PieceColor color) {
		Point p = findKing(color);
		return canBeTaken(p);
	}

	/*
	Iterates through the board and determines all possible moves for a specific color
	@param color - the color of the pieces which should be checked for moves
	@return - a list of all possible moves as
	 */
	public ArrayList<Tuple<Point, Point>> findAllMoves(PieceColor color) {
		ArrayList<Tuple<Point, Point>> list = new ArrayList<>();
		for (int x = 0; x < 8; x++) {
			for (int y = 0; y < 8; y++) {	// iterate over the entire board
				if (at(x, y) != null && at(x, y).colorMatches(color))
					list.addAll(findAllMoves(x, y));
			}
		}
		return list;
	}

	/*
	Returns all possible moves from the given location
	@param x, y - the location to check
	@return - an empty list if the point is not valid or a piece is null, otherwise a list of all locations
	the piece residing at that location
	 */
	public ArrayList<Tuple<Point, Point>> findAllMoves(int x, int y) {
		ArrayList<Tuple<Point, Point>> list = new ArrayList<>();
		if (at(x, y) == null)
			return list;
		Piece p = at(x, y);	// p is not null
		Point from = new Point(x, y);

		FindMoveFunction stop = new FindMoveFunction(from, p.getColor(), true);
		FindMoveFunction dontStop = new FindMoveFunction(from, p.getColor(), false);
		switch(p.iden()) {
			case 'P':
				return findPawnMoves(from);
			case 'Q':
				checkDiagonals(from, stop);
				checkLines(from, stop);
				return stop.getMoves();
			case 'K':
				// check surrounding squares for places king can take
				adjacent(from, new Function() {
					@Override
					public boolean run(int x, int y) {
						if (kingCanTake(from, new Point(x, y))) {
							list.add(new Tuple(from, new Point(x, y)));
						}
						return true;
					}
				});
				return list;
			case 'R':
				checkLines(from, stop);
				return stop.getMoves();
			case 'B':
				checkDiagonals(from, stop);
				return stop.getMoves();
			case 'N':
				knightMoves(from, dontStop);
				return dontStop.getMoves();

		}
		return list;
	}

	/*
	Finds all possible moves for the pawn at the location passed in
	@param location - the location of the pawn
	@return - an arraylist of tuples for every possible move the pawn can make
	 */
	public ArrayList<Tuple<Point, Point>> findPawnMoves(Point location) {
		ArrayList<Tuple<Point, Point>> list = new ArrayList<>();
		// check degenerate cases
		Piece pawn = at(location);
		if (!isValid(location) || pawn == null || pawn.iden() != 'P')
			return list;

		int yModifier = (pawn.colorMatches(PieceColor.light) ? 1 : -1);

		// check for forward movement
		Point p2 = new Point(location.x, location.y + yModifier);
		Point p3 = new Point(location.x, location.y + 2 * yModifier);

		if (isValid(p2) && at(p2) == null) {
			list.add(new Tuple(location, p2));
			if (atStartingRow(pawn) && at(p3) == null)
				list.add(new Tuple(location, p3));
		}

		// check for diagonal movement
		Point p4 = new Point(location.x + 1, location.y + yModifier);
		Point p5 = new Point(location.x - 1, location.y + yModifier);

		if (isValid(p4) && at(p4) != null && !pawn.colorMatches(at(p4))){
			list.add(new Tuple(location, p4));
		}
		if (isValid(p5) && at(p5) != null && !pawn.colorMatches(at(p5))) {
			list.add(new Tuple(location, p5));
		}

		// check for en-passant
		if (isValid(p4)) {
			Point taken = new Point(location.x + 1, location.y);
			if ( at(p4) != null && !pawn.colorMatches(at(p4))
				|| ( at(p4) == null && !pawn.colorMatches(at(taken))) )
				list.add(new Tuple(location, p4));
		}

		if (isValid(p5)) {
			Point taken = new Point(location.x + 1, location.y);
			if ( at(p5) != null && !pawn.colorMatches(at(p5))
				|| at(p5) == null && !pawn.colorMatches(at(taken)) )
				list.add(new Tuple(location, p5));
		}

		return list;
	}

	/*
	Determines if the piece at the given point can be taken by anything
	@param p - the point of the piece to be checked
	@return - a boolean representing whether or not hte piece at the given location can be taken
	 */
	public boolean canBeTaken(Point p) {
		return canBeTaken(at(p));
	}

	/*
	Determines if the given piece can be taken from anything
	Note: default behaviour is to return false if the piece is null.
	@param p - the peice to be checked
	@return - a boolean representing whether or not the piece passed in can be taken
	 */
	public boolean canBeTaken(Piece p) {
		if (p == null)
			return false;

		Point location = p.getPoint();		// the point of the piece
		PieceColor color = (p.getColor() == PieceColor.light) ? PieceColor.dark : PieceColor.light;


		// checks straight paths
		straightPieces.reset(color);
		upX(location, straightPieces);
		upY(location, straightPieces);
		downX(location, straightPieces);
		downY(location, straightPieces);
		if (straightPieces.foundPiece())
			return true;

		// checks diagonal paths
		diagPieces.reset(color);
		ascXandY(location, diagPieces);
		ascXdescY(location, diagPieces);
		descXandY(location, diagPieces);
		descXascY(location, diagPieces);
		if (diagPieces.foundPiece())
			return true;

		// checks for knights
		knightPiece.reset(color);
		knightMoves(location, knightPiece);
		if (knightPiece.foundPiece())
			return true;

		// checks for diagonal pawns
		pawnPiece.reset(color);
		if (color == PieceColor.light)
			backwardsDiagonals(location, pawnPiece);
		else
			forwardDiagonals(location, pawnPiece);
		if (pawnPiece.foundPiece())
			return true;

		// checks whether or not a king can take this piece
		kingPiece.reset(color);
		adjacent(location, kingPiece);
		ArrayList<Point> points = kingPiece.getPoints();

		ListIterator iter = points.listIterator();
		while (iter.hasNext()) {
			Point origLocation = (Point)iter.next();
			Piece king = at(origLocation);
			if (king == null || king.iden() != 'K') {
				System.err.println("Something went wrong when examining whether or not a king could take.");
				return true;
			}

			if (!kingCanTake(origLocation, location))
				iter.remove();

		}
		if (points.size() > 0)
			return true;
		return false;
	}

	/*
	----------------------------
	Misc. Functionality Methods
	----------------------------
	 */

	/*
	Performs the given move (without consideration for validity or legality) and then runs func and returns
	the output
	@param origLocation - the location of the piece moving
	@param location - the location to move the piece to
	@param func - the function to be run after the move is done
	@return - the return value of func
	 */
	public boolean testMove(Point origLocation, Point location, Function func) {
		Piece atOrig = at(origLocation);
		Piece taken = at(location);

		setLocation(null, origLocation.x, origLocation.y);
		setLocation(atOrig, location.x, location.y);

		boolean result = func.run(location.x, location.y);

		setLocation(atOrig, origLocation.x, origLocation.y);
		setLocation(taken, location.x, location.y);

		return result;
	}

	/*
	----------------------------
	Private Functions
	----------------------------
	 */

	/*
	A standard function which will continue searching until a piece of the opposite color is found, and
	stop at the first piece found if the stopAtFirstPiece boolean is set.
	 */
	private class SearchFunc implements Function{

		private char[] identifiers;			// the piece identifiers to search for
		private boolean stopAtFirstPiece;	// should we stop at the first piece we see
		private PieceColor color;			// the color of the piece being searched for
		private ArrayList<Point> points = new ArrayList<Point>();	// all points where the pieces are found

		SearchFunc(char[] identifiers, boolean stopAtFirstPiece, PieceColor color) {
			this.identifiers = identifiers;
			this.stopAtFirstPiece = stopAtFirstPiece;
			this.color = color;
		}

		public void reset(PieceColor color) {
			this.color = color;
			points = new ArrayList<>();
		}

		public boolean foundPiece() {
			return points.size() > 0;
		}

		public Point getFirstPoint() {
			if (!foundPiece())
				return null;
			return points.get(0);
		}

		public Piece getFirstPiece() {
			if (!foundPiece())
				return null;
			return at(points.get(0));
		}

		public ArrayList<Point> getPoints() {
			return points;
		}

		@Override
		public boolean run(int x, int y) {
			Piece p = at(x, y);	// finds the piece at the point

			// tests if the piece is the correct color and matches one of the identifiers
			if (p != null && p.getColor() == color && identifiersContains(p.iden())) {
				// we found the piece being searched for
				points.add(new Point(x, y));
			}

			// tests if we should stop yet
			if (stopAtFirstPiece && p != null)
				return false;
			return true;
		}

		private boolean identifiersContains(char c) {
			for (char iden : identifiers) {
				if (iden == c) return true;
			}
			return false;
		}
	}

	/*
	An implementation of Function which will find all possible locations
	 */
	private class FindMoveFunction implements Function {

		// all possible move locations
		private ArrayList<Tuple<Point, Point>> points = new ArrayList<>();
		private PieceColor myColor;
		private Point origin;
		private boolean stopAtFirstPiece;

		public FindMoveFunction(Point from, PieceColor myColor, boolean stopAtFirstPiece) {
			this.myColor = myColor;
			this.origin = from;
			this.stopAtFirstPiece = stopAtFirstPiece;
		}

		@Override
		public boolean run(int x, int y) {
			Piece p = at(x, y);
			if (p == null) {	// we can move to an empty space
				points.add(new Tuple<Point, Point>(origin, new Point(x, y)));
				return true;
			}
			if (myColor != p.getColor() && p.iden() != 'K')	// if the color is different, we can take it
				points.add(new Tuple<Point, Point>(origin, new Point(x, y)));
			return !stopAtFirstPiece;		// should we continue looking?
		}

		public ArrayList<Tuple<Point, Point>> getMoves() {
			return points;
		}

	}

	/*
	A function to determine whether or not the game is impossible to end.
	 */
	public boolean inInfiniteGame() {
		// test for only kings left

		for (int x = 0; x < 8; x++)
			for (int y = 0; y < 8; y++)
				if (at(x, y) != null && at(x, y).iden() != 'K')
					return false;

		return true;
	}

	/*
	------------------------------------------------------------------------------------------------
	Parameterized board traversal functions
	------------------------------------------------------------------------------------------------
	 */



	/*
	Combines and runs all line movements
	 */
	public static void checkLines(Point p, Function func) {
		upX(p, func);
		upY(p, func);
		downY(p, func);
		downX(p, func);
	}

	/*
	Combines and runs all diagonal movements
	 */
	public static void checkDiagonals(Point p, Function func) {
		ascXdescY(p, func);
		ascXandY(p, func);
		descXascY(p, func);
		descXandY(p, func);
	}

	/*
	A method which will perform the lambda expression while moving up the given row, returning once
	the lambda expression returns false.
	@param x - the point to begin iterating from
	@param func - a function to be run on each x, which has a return value representing whether
	the next square should be examined.
	@return - the last point examined is returned.
	Note: If an illegal x or y is input, then the function will immediately return the initial point
	 */
	public static Point upX(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x + 1, point.y);});
	}


	/*
	Same method as upX; however, works downwards
	 */
	public static Point downX(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x - 1, point.y);});
	}

	/*
	Same method as upX; however, works upwards in Y
	 */
	public static Point upY(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x, point.y + 1);});
	}


	/*
	Same method as upX; however, works downwards in Y
	 */
	public static Point downY(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x, point.y - 1);});
	}

	/*
	Same method as upX; however, goes along the ascending diagonal
	 */
	public static Point ascXandY(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x + 1, point.y + 1);});
	}


	/*
	Same method as upX; however, goes along the descending diagonal
	 */
	public static Point descXandY(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x - 1, point.y - 1);});
	}

	/*
	Same method as upX; however, goes along the alternating ascending diagonal
	 */
	public static Point descXascY(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x - 1, point.y + 1);});
	}

	/*
	Same method as upX; however, goes along the alternating descending diagonal
	 */
	public static Point ascXdescY(Point p, Function func) {
		return checkPoints(p, func, (Point point) -> { point.setLocation(point.x + 1, point.y - 1);});
	}

	/*
	Basic interface for iterating over points
	 */
	interface PointIterator {
		void updatePoint(Point point);	// iterates the point passed in to the next point
	}

	/*
	Iterates through the point using the PointIterator given, starting from p, checking with f
	@param p - the point to start from (this point is not checked)
	@param f - the function to run on each point (return true if you want to continue iterating)
	@param nextPoint - the point iterator to get our next point
	@return - the last point checked
	 */
	public static Point checkPoints(Point p, Function f, PointIterator nextPoint) {
		Point p2 = (Point)p.clone();
		nextPoint.updatePoint(p2);
		while (isValid(p2) && f.run(p2.x, p2.y)) {
			nextPoint.updatePoint(p2);
		}
		return p2;
	}

	/*
	Same method as upX; however, checks surrounding squares
	 */
	public static Point adjacent(Point p, Function func) {
		Point p2 = null;
		for (int x = -1; x <= 1; x++) {
			for (int y = -1; y <= 1; y++) {
				if ((x == y && x == 0) || !isValid(p))
					continue;

				p2 = new Point(p.x + x, p.y + y);
				if (isValid(p2) && !func.run(p.x + x, p.y + y))
					return p2;
			}
		}
		return new Point(p.x + 1, p.y + 1);
	}

	/*
	Same method as upX; however, checks the knight movement squares
	 */
	public static Point knightMoves(Point p, Function func) {
		Point p2 = null;
		for (int x = -2; x <= 2; x++) {
			for (int y = -2; y <= 2; y++) {
				if ( (x == 0 || y== 0) || (Math.abs(x) == 1 && Math.abs(y) == 1) || (Math.abs(x) == 2 && Math.abs(y) == 2) )
					continue;

				p2 = new Point(p.x + x, p.y + y);
				if (isValid(p2) && !func.run(p2.x, p2.y))
					return p2;

			}
		}
		return p2;
	}

	/*
	General method which tests the exact points passed in
	@points - an arraylist of all the points to be tested, int he order to test them
	@func = the function that tests the individual points
	@return - null if the size of the arraylist passed in is 0
	 */
	public static Point testPoints(Point[] points, Function func) {
		for (Point p : points) {
			if (isValid(p) && !func.run(p.x, p.y))
				return p;
		}

		if (points.length == 0)
			return null;

		return points[points.length - 1];
	}

	/*
	Same method as upX; however, checks forward diagonals
	 */
	public static Point forwardDiagonals(Point p, Function func) {
		Point p2 = new Point(p.x - 1, p.y + 1);
		Point p3 = new Point(p.x + 1, p.y + 1);
		Point[] arr = {p2, p3};
		return testPoints(arr, func);
	}

	/*
	Same method as upX; however, checks backwards diagonals
	 */
	public static Point backwardsDiagonals(Point p, Function func) {
		Point p2 = new Point(p.x - 1, p.y - 1);
		Point p3 = new Point(p.x + 1, p.y - 1);
		Point[] arr = {p2, p3};
		return testPoints(arr, func);
	}

	/*
	Same method as upX; however, checks the two squares directly in front
	 */
	public static Point twoForward(Point p, Function func) {
		Point p2 = new Point(p.x, p.y + 1);
		Point p3 = new Point(p.x, p.y + 2);
		Point[] arr = {p2, p3};
		return testPoints(arr, func);
	}

	/*
	Same method as upX; however, checks the two squares directly behind
	 */
	public static Point twoBackwards(Point p, Function func) {
		Point p2 = new Point(p.x, p.y - 1);
		Point p3 = new Point(p.x, p.y - 2);
		Point[] arr = {p2, p3};
		return testPoints(arr, func);
	}

}