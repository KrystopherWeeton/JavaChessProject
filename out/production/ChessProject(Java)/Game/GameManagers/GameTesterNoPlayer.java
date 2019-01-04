package Game.GameManagers;

import Func.*;
import Game.Pieces.*;

import java.awt.*;
import java.util.logging.*;

public class GameTesterNoPlayer extends GameManagerNoPlayer {

	private final boolean displayFinalResults;

	public GameTesterNoPlayer(boolean displayFinalResults) {
		this.displayFinalResults = displayFinalResults;
	}

	final private static Logger logger = Logger.getLogger("GameManager");

	@Override
	protected void handleGameOver() {
		logger.config("Entering handleGameOver in GameTesterNoPlayer");

		if (displayFinalResults) {
			handleBoardUpdate();
			String result = (isWhitesTurn() ? "White" : "Black") + " has won the game! Returning to the main menu.";
			GUIFunctionality.sendWarning(result, this);
		}

		this.dispose();
	}
}
