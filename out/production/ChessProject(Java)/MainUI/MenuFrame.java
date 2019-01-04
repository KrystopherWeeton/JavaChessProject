package MainUI;

import javax.swing.BoxLayout;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Point;
import java.util.logging.*;
import java.awt.Graphics;

// Uses all classes in func
import Func.*;
import Game.GameManagers.*;


public class MenuFrame extends HidingJFrame{

	private JButton play;				// the play button
	private JButton load_game;			// the button to load a game
	private JButton stress_test;		// the button to stress test the AI with a number of randomly played games.

	final private static Dimension DEFAULT_DIMENSION = new Dimension(200, 300);
	final private static Point DEFAULT_LOCATION = GUIFunctionality.getLocationToCenter(DEFAULT_DIMENSION);
	final private static Dimension DEFAULT_BUTTON_SIZE = new Dimension(100, 50);

	final private static Logger logger = Logger.getLogger("Logger");

	final private static boolean pauseAtEndOfStressTesting = true;

	/*
	Constructs the menu frame and all sub-items
	 */
	public MenuFrame() {

		// creates self and sets layout
		super("Chess Game");
		logger.config("Creating new MenuFrame");

		// initializes traits for JPanel
		this.setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));
		this.setSize(MenuFrame.DEFAULT_DIMENSION);
		this.setLocation(DEFAULT_LOCATION);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// creates the main menu image
		this.setContentPane(new ImagePanel("MenuImages/MainMenuPicture.png"));

		// creates the buttons
		play = GUIFunctionality.makeButton("Play", DEFAULT_BUTTON_SIZE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle_play(e);
			}
		});
		play.setAlignmentX(JButton.CENTER);
		this.add(play);

		load_game = GUIFunctionality.makeButton("Load Game", DEFAULT_BUTTON_SIZE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				handle_load(e);
			}
		});
		load_game.setAlignmentX(JButton.CENTER);
		this.add(load_game);

		stress_test = GUIFunctionality.makeButton("Test", DEFAULT_BUTTON_SIZE, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				runStressTests(e);
			}
		});
		stress_test.setAlignmentX(JButton.CENTER);
		this.add(stress_test);


		// sets this as visible and displays to user
		this.setVisible(true);
	}

	private void runStressTests(ActionEvent e) {
		logger.config("Entering stress_test from the main menu.");

		// Gets user input for the number of tests to run
		String numTests = JOptionPane.showInputDialog("Enter the number of tests");

		while (numTests == null || Integer.valueOf(numTests) == null) {
			numTests = JOptionPane.showInputDialog("The input had no numbers, please input the number of test to run.");
		}
		Integer tests = Integer.valueOf(numTests);

		StressTestResults results;
		int totalMoves = 0;
		// Iterate through each test
		for (int i = 0; i < tests; i++) {
			results = doStressTest();
			totalMoves += results.moves;
		}

		// Displays information from the tests to the user
		System.out.println("Average number of moves is " + (totalMoves / tests));
	}

	private class StressTestResults {
		public int moves = 0;
	}

	private StressTestResults doStressTest() {

		// initializes the game manager to begin playing
		GameManager gm = new GameTesterNoPlayer(pauseAtEndOfStressTesting);
		gm.addWindowListener(this);
		gm.setVisible(true);

		// while loop which repeatedly runs the game while the game is active
		StressTestResults results = new StressTestResults();
		Graphics g = gm.getGraphics();
		while (gm.isDisplayable()) {
			results.moves++;
			gm.actionPerformed("");
		}
		return results;
	}

	/*
	Starts the game of chess. Is called when the play button is pressed
	 */
	private void handle_play(ActionEvent e) {
		logger.config("Entering handle_play");
		String[] options = {"0", "1", "2"};
		int players = JOptionPane.showOptionDialog(this,
				"How many human players in your game?",
				"New Game",
				JOptionPane.YES_NO_CANCEL_OPTION,
				JOptionPane.QUESTION_MESSAGE,
				null,
				options,
				options[0] );


		GameManager gm = null;
		switch (players) {
			case 0: gm = new GameManagerNoPlayer();
					break;
			case 1: gm = new GameManagerOnePlayer();
					break;
			case 2: gm = new GameManagerTwoPlayer();
					break;
			default:
				return;
		}

		gm.addWindowListener(this);			// sets main menu to hide until game is over
		gm.setVisible(true);					// let's play a game
	}

	@Override
	public void windowOpened(WindowEvent e) {
		super.windowClosed(e);
	}

	@Override
	public void windowClosed(WindowEvent e) {
		super.windowClosed(e);
	}

	/*
	Starts the process of loading a game. Is called when the load game button is pressed
	 */
	private void handle_load(ActionEvent e) {
		logger.config("Entering handle_load");
		JOptionPane.showMessageDialog(this,
				"This feature has not been implemented yet.");
	}
}