package Game.GameManagers;

import Func.*;

import java.util.logging.*;

public class GameTesterNoPlayer extends GameManagerNoPlayer {

	final private static Logger logger = Logger.getLogger("GameManager");

	@Override
	protected void handleGameOver() {
		logger.config("Entering handleGameOver in GameTesterNoPlayer");
		handleBoardUpdate();
		String result = (isWhitesTurn() ? "White" : "Black") + " has won the game! Returning to the main menu.";
		GUIFunctionality.sendWarning(result, this);
		this.dispose();
	}
}
