package Game;

import javax.swing.*;
import java.awt.*;
import java.util.logging.*;

import Func.Location;
import Game.Pieces.Piece;

/*
A class which manages the gui for the game (includes outside
buttons etc.)
 */
public class GameGuiManager extends JPanel{

	/*
	-------------------
	Fields
	-------------------
	 */

	// Handles displaying the board, as well as gathering user input from the board
	private BoardGuiManager boardGuiManager;

	// Displays the response to the most recent move in the game
	private JLabel moveResponse = new JLabel("moveResponse");

	// Labels to display the point totals in the game
	private JLabel lowerPoints = new JLabel("lowerPoints", JLabel.CENTER);
	private JLabel upperPoints = new JLabel("upperPoints", JLabel.CENTER);

	// Buttons for game iteration
	private JButton stepForward = new JButton("Step");
	private JButton stepBackwards = new JButton("Back");
	private JButton beginning = new JButton("Beginning");
	private JButton end = new JButton("Current Board");

	// Spring layout to handle UI
	private SpringLayout layout = new SpringLayout();

	// Constants for ease of use
	final private static String WEST = SpringLayout.WEST;
	final private static String EAST = SpringLayout.EAST;
	final private static String NORTH = SpringLayout.NORTH;
	final private static String SOUTH = SpringLayout.SOUTH;

	// Static constants
	final private static Dimension DEFAULT_SIZE = new Dimension(900, 900);

	// Logger for logging
	final private static Logger logger = Logger.getLogger("GameGuiManager");

	/*
	----------------------------
	Constructors and Generators
	----------------------------
	 */

	public GameGuiManager (GameListener inputListener) {

		// sets initial ui qualities
		this.setSize(DEFAULT_SIZE);
		this.setPreferredSize(DEFAULT_SIZE);
		this.setLayout(layout);

		// create components
		boardGuiManager = new BoardGuiManager(inputListener);

		// add components
		this.add(boardGuiManager);
		this.add(moveResponse);
		this.add(lowerPoints);
		this.add(upperPoints);
		this.add(stepForward);
		this.add(stepBackwards);
		this.add(beginning);
		this.add(end);

		// set constraints of components

		// put the boardGUI at (5, 5)
		layout.putConstraint(WEST, boardGuiManager, 5, WEST, this);
		layout.putConstraint(NORTH, boardGuiManager, 5, NORTH, this);

		// put the moveResponse directly below the board
		layout.putConstraint(WEST, moveResponse, 20, WEST, this);
		layout.putConstraint(NORTH, moveResponse, 5, SOUTH, boardGuiManager);

		// put the upperPoints into location
		layout.putConstraint(NORTH, upperPoints, 10, NORTH, boardGuiManager);
		layout.putConstraint(WEST, upperPoints, 5, EAST, boardGuiManager);

		// put the lowerPoints into location
		layout.putConstraint(SOUTH, lowerPoints, -10, SOUTH, boardGuiManager);
		layout.putConstraint(WEST, lowerPoints, 5, EAST, boardGuiManager);

		// put the top of the buttons into position
		layout.putConstraint(NORTH, beginning, 10, NORTH, this);
		layout.putConstraint(WEST, beginning, 30, EAST, boardGuiManager);

		// put the step buttons into position
		layout.putConstraint(NORTH, stepForward, 5, SOUTH, beginning);
		layout.putConstraint(WEST, stepForward, 0, WEST, beginning);
		layout.putConstraint(NORTH, stepBackwards, 5, SOUTH, beginning);
		layout.putConstraint(WEST, stepBackwards, 0, EAST, stepForward);

		// put the end button into place
		layout.putConstraint(NORTH, end, 5, SOUTH, stepForward);
		layout.putConstraint(WEST, end, 0, WEST, beginning);

		// set everything to visible
		lowerPoints.setVisible(true);
		upperPoints.setVisible(true);
		moveResponse.setVisible(true);
		stepForward.setVisible(true);
		stepBackwards.setVisible(true);
		beginning.setVisible(true);
		end.setVisible(true);
		this.setVisible(true);

	}

	/*
	----------------------------
	Modifiers / Updaters
	----------------------------
	 */

	/*
	Sets the moveResponse label to reflect the text passed in
	@param text - the text to be displayed
	 */
	public void setMoveResponse(String text) {
		moveResponse.setText(text);
		moveResponse.setSize(moveResponse.getPreferredSize());
		moveResponse.repaint();
	}

	/*
	Sets the upperPoints label to reflect the text passed in
	@param text - the text to be displayed
	 */
	public void setUpperPoints(String text) {
		upperPoints.setText(text);
		upperPoints.repaint();
	}

	// Sets upperPoints to the given value
	public void setUpperPoints(int value) {
		setUpperPoints(Integer.toString(value));
	}

	/*
	Sets the lowerPoints label to reflect the text passed in
	@param text - the text to be displayed
	 */
	public void setLowerPoints(String text) {
		lowerPoints.setText(text);
		lowerPoints.repaint();
	}

	// Sets lowerPoints to the given value
	public void setLowerPoints(int value) {
		setLowerPoints(Integer.toString(value));
	}

	public void setPoints(int upper, int lower) {
		setUpperPoints(upper);
		setLowerPoints(lower);
	}

	/*
	Draws the board to the display
	@param pieces - the board to draw
	@param whitesTurn - true if it is whites turn, false otherwise.
	 */
	public void redrawBoard(Piece[][] pieces, boolean whitesTurn, int lightPoints, int darkPoints) {
		logger.config("Entering redrawBoard in GameGuiManager");
		boardGuiManager.redrawBoard(pieces, whitesTurn);
		if (whitesTurn) {
			setPoints(darkPoints, lightPoints);
		} else
			setPoints(lightPoints, darkPoints);
	}

	/*
	Selects a square, setting it's background to the highlighted color
	@param p - the square to select
	 */
	public boolean selectSquare(Point p) {
		return boardGuiManager.selectSquare(p);
	}

	/*
	Deselects a square, setting it's background back to the default color
	@param p - the square to deselect
	 */
	public boolean deselectSquare(Point p) {
		return boardGuiManager.deselectSquare(p);
	}

}