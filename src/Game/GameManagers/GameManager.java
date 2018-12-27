package Game.GameManagers;

import Func.*;
import Game.*;
import Game.Exceptions.*;
import Game.Pieces.*;
import java.util.logging.*;

import javax.swing.*;
import java.awt.*;

/*
A high level manager for the game processes. Should be overwritten for implementation.
The methods actionPerformed, and redrawBoard should be overwritten
 */
public abstract class GameManager extends JFrame implements GameListener {

	/*
	-------------------------------
	Fields
	-------------------------------
	 */

	// Responsibilities are managing the UI for the game
	protected GameGuiManager gameGuiManager;

	// Responsibilities are enforcing rules and maintaining the board
	protected BoardManager boardManager;

	// true = it is white's turn, false = it is black's turn
	private boolean whitesTurn = true;

	// this is the default size of the board
	final private static Dimension DEFAULT_SIZE = new Dimension(900, 750);

	final private static Logger logger = Logger.getLogger("GameManager");

	/*
	-------------------------------
	Constructors
	-------------------------------
	 */

	public GameManager() {
		super("Chess Game");
		this.setSize(DEFAULT_SIZE);
		this.setLocation(GUIFunctionality.getLocationToCenter(DEFAULT_SIZE));
		gameGuiManager = new GameGuiManager(this);
		this.setContentPane(gameGuiManager);
		this.pack();
		boardManager = new BoardManager();
		redrawBoard();

		logger.finer("GameManager created");
	}

	/*
	-------------------------------
	Overwritten functionality
	-------------------------------
	 */

	/*
	Called when there is actionPerformed by the user on the board. Determines how to proceed.
	@param actionCode - the actionCode given by the translateString method
	 */
	@Override
	abstract public void actionPerformed(String actionCode);

	/*
	Redraws the board, however the GameManager chooses to redraw it.
	 */
	abstract public void redrawBoard();

	/*
	-------------------------------
	Base Functionality
	-------------------------------
	 */

	/*
	Made to allow for base functionality when redrawing the board, before the abstract method is called
	 */
	protected void handleBoardUpdate() {
		redrawBoard();
	}

	/*
	Returns the color of the active turn
	 */
	protected PieceColor color(boolean whitesTurn) {
		return (whitesTurn) ? PieceColor.light : PieceColor.dark;
	}

	/*
	Accessor for whitesTurn for derived classes
	 */
	public boolean isWhitesTurn() {
		return whitesTurn;
	}

	/*
	Interprets the move by sending it to the boardManager, and then determining what to do from there
	@param from - the point that is being moved from
	@param to - the point that is being moved to
	@return a boolean representing whether or not the move was successful
	 */
	protected boolean interpretMove(Point from, Point to) {
		logger.config("Entering interpretMove with " +
				"from: " + from + " to: " + to);
		try {
			// determines whether or not the move is of the right color
			if (!boardManager.colorMatches(from, color(whitesTurn))) {
				// attempt to move wrong color piece
				gameGuiManager.setMoveResponse("It is " + (whitesTurn ? "White's" : "Black's") + " turn.");
				return false;
			}

			// performs the move on the board
			boolean gameOver = boardManager.performMove(from, to);

			// if we get here, then the move is definitely valid and the board (and ui) has been updated
			if (gameOver) {
				handleGameOver();
			} else {	// switch turns and redraw the board
				whitesTurn = !whitesTurn;
				handleBoardUpdate();
			}

			// reset the message on the displayLabel
			gameGuiManager.setMoveResponse("");
			return true;

		} catch (BoardIndexException e) {	// this type of exception should not be occuring
			e.printStackTrace();
			logger.warning("Detected BoardIndexException in GameManager interpretMove method.");
		} catch (InvalidMoveException e) {
			gameGuiManager.setMoveResponse("That move is invalid. Try another.");
			logger.config("Invalid move entered.");
			return false;
		} catch (IllegalMoveException e) {
			gameGuiManager.setMoveResponse("That move is illegal. Try another.");
			logger.config("Illegal move entered.");
			return false;
		}
		logger.warning("Hit end of interpretMove method.");
		return false;	// should never hit this point
	}

	/*
	What to do in the event of a game over. This can be overwritten in derived classes to allow for custom functionality;
	however, a default behaviour is specified which tells the user that the game is over and then returns to the main
	menu.
	 */
	protected void handleGameOver() {
		logger.info("Game over has been detected.");
		// handle starting a new game here
		handleBoardUpdate();
		String result = (whitesTurn ? "White" : "Black") + " has won the game! Returning to the main menu.";
		GUIFunctionality.sendWarning(result, this);
		this.dispose();
	}

	/*
	Translates the given string into a point on the board, returning a null point
	if the string does not fit the requirements
	@param str must be a string of length 2 with each character being a digit 0-8 representing
	the x and y coordinate in the board
	 */
	public Point translateString(String str){
		logger.config("Entering translateString");
		if (str.length() < 2)
			return null;
		char xChar = str.charAt(0);
		char yChar = str.charAt(1);
		int x = xChar - (int)'0';
		int y = yChar - (int)'0';
		if (x < 0 || y < 0 || x > 7 || y > 7)
			return null;
		return new Point(x, y);
	}


}