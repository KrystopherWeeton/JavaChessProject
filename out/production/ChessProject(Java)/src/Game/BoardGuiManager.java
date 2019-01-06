package Game;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

import Func.*;
import Game.Pieces.Piece;
import java.util.logging.*;

/*
A class which actually handles display, user input, etc.
for the board of the game itself
 */

public class BoardGuiManager extends JPanel implements ActionListener{

	// The BoardListener to be notified of user input on the board
	private GameListener inputListener;

	// Constants to be used
	final private static Dimension DEFAULT_SIZE = new Dimension(550, 550);
	final private static Color DARK_SQUARE_COLOR = Color.darkGray;
	final private static Color LIGHT_SQUARE_COLOR = Color.lightGray;
	final private static Color SELECTED_COLOR = Color.ORANGE;

	// Handles the display of each square
	private GridButton[][] board = new GridButton[ 8 ][ 8 ];
	private JLabel[] xLabels = new JLabel[8];
	private JLabel[] yLabels = new JLabel[8];

	private boolean lightDisplayed = true;

	final private static Logger logger = Logger.getLogger("BoardGuiManager");

	final private static boolean LOGGING = Consts.LOGGING;


	public BoardGuiManager(GameListener inputListener) {
		super(new GridLayout(9, 9));
		if (LOGGING)
			logger.config("Creating boardGuiManager");
		 this.setBorder(new LineBorder(Color.black));
		this.inputListener = inputListener;

		this.setSize(DEFAULT_SIZE);
		this.setPreferredSize(DEFAULT_SIZE);
		this.setMinimumSize(DEFAULT_SIZE);
		this.setMaximumSize(DEFAULT_SIZE);

		generateUI();
	}

	public void redrawPiece(Piece p, int prevX, int prevY) {

		JButton oldLocation = board[prevX][prevY];
		JButton newLocation = board[p.getX()][p.getY()];

		oldLocation.setIcon(null);
		newLocation.setIcon(p.getImage());

		oldLocation.repaint();
		newLocation.repaint();
	}

	public void redrawBoard(Piece[][] board, boolean whitesTurn) {
		if (LOGGING)
			logger.config("Entering redrawBoard");
		this.setVisible(false);
		if (whitesTurn) {
			redrawWhite(board);
			lightDisplayed = true;
		} else {
			redrawBlack(board);
			lightDisplayed = false;
		}
		this.setVisible(true);
	}

	/*
	Draws the board from the perspective of the white player, i.e. 00 on bottom left
	 */
	private void redrawWhite(Piece[][] board) {
		for(int y = 0; y < 8; y++ ) {
			for (int x = 0; x < 8; x++) {
				if (board[x][y] == null)
					this.board[x][y].setIcon(null);
				else
					this.board[x][y].setIcon(board[x][y].getImage());
				this.board[x][y].repaint();
			}
		}
		for (int i = 0; i < 8; i++) {
			xLabels[i].setText(Character.toString((char)('A' + i)));
			yLabels[i].setText(Integer.toString(i + 1));
		}
	}

	/*
	Draws the board from the perspective of the black player, i.e. 00 on top right
	 */
	private void redrawBlack(Piece[][] board) {
		for(int y = 0; y < 8; y++ ) {
			for (int x = 0; x < 8; x++) {
				if (board[7 - x][7 - y] == null)
					this.board[x][y].setIcon(null);
				else
					this.board[x][y].setIcon(board[7 - x][7 - y].getImage());
				this.board[x][y].repaint();
			}
		}
		for (int i = 0; i < 8; i++) {
			xLabels[i].setText(Character.toString((char)('A' + 7 - i)));
			yLabels[i].setText(Integer.toString(8 - i));
		}
	}

	/*
	Generates the UI to be used for displaying the board; however, you want to call
	redrawBlack / redrawWhite before you actually view the board as everything will be
	missing if you don't.
	 */
	private void generateUI() {
		if (LOGGING)
			logger.config("Entering generateUI");
		int squareLength = this.getWidth() / 8;
		Dimension squareSize = new Dimension(squareLength, squareLength);

		// empty square on the top right
		this.add(generateLabel("", squareSize));

		// generate the first row (i.e. column labels)
		for (int c = 0; c < 8; c++) {
			xLabels[c] = generateLabel((char)('A' + c), squareSize);
			this.add(xLabels[c]);
		}

		// generate the squares of the grid along with the row labels
		Dimension d = new Dimension(squareLength, squareLength);
		for (int y = 7; y >= 0; y--) {
			yLabels[y] = generateLabel(Integer.toString(y + 1), squareSize);
			this.add(yLabels[y]);
			for (int x = 0; x < 8; x++) {
				Color c = findColor(x, y);

				GridButton square = new GridButton(c, d);

				square.setActionCommand(Integer.toString(x) + Integer.toString(y));
				square.addActionListener(this);

				this.add(square);
				board[x][y] = square;
			}
		}
	}

	private JLabel generateLabel(char text, Dimension size) {
		return generateLabel(Character.toString(text), size);
	}

	private JLabel generateLabel(String text, Dimension size) {
		JLabel label = new JLabel(text, SwingConstants.CENTER);
		label.setSize(size);
		label.setPreferredSize(size);
		return label;
	}

	private Color findColor(int x, int y) {
		return ( (x + y) % 2 == 1) ? LIGHT_SQUARE_COLOR : DARK_SQUARE_COLOR;
	}

	public boolean selectSquare(Point p) {
		if (LOGGING)
			logger.config("Selecting " + p);
		if (!BoardManager.isValid(p))
			return false;
		board[p.x][p.y].setBackground(SELECTED_COLOR);
		return true;
	}

	public boolean deselectSquare(Point p) {
		if (LOGGING)
			logger.config("Deselecting " + p);
		if (!BoardManager.isValid(p))
			return false;
		board[p.x][p.y].setBackground(findColor(p.x, p.y));
		return true;
	}

	/*
	Translates the action code into the actual location clicked on the board and then sends it to
	the listener
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		String s2 = e.getActionCommand();
		if (lightDisplayed == false) {
			s2 = Integer.toString(7 - Integer.valueOf(s2.substring(0, 1))) +
					Integer.toString(7 - Integer.valueOf(s2.substring(1, 2)));
		}
		inputListener.actionPerformed(s2);
	}
}