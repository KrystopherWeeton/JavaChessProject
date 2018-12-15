package MainUI;

import javax.swing.BoxLayout;
import java.awt.event.*;
import javax.swing.*;
import java.awt.Dimension;
import java.awt.Point;

// Uses all classes in func
import Func.*;
import Game.GameManagers.*;


public class MenuFrame extends HidingJFrame{

	private JButton play;				// the play button
	private JButton load_game;			// the button to load a game

	final private static Dimension DEFAULT_DIMENSION = new Dimension(200, 300);
	final private static Point DEFAULT_LOCATION = GUIFunctionality.getLocationToCenter(DEFAULT_DIMENSION);
	final private static Dimension DEFAULT_BUTTON_SIZE = new Dimension(100, 50);

	/*
	Constructs the menu frame and all sub-items
	 */
	public MenuFrame() {

		// creates self and sets layout
		super("Chess Game");
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


		// sets this as visible and displays to user
		this.setVisible(true);
	}

	/*
	Starts the game of chess. Is called when the play button is pressed
	 */
	private void handle_play(ActionEvent e) {

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

		JOptionPane.showMessageDialog(this,
				"This feature has not been implemented yet.");
	}
}