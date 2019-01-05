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


	@Override
	protected void handleGameOver() {

		if (displayFinalResults) {
			handleBoardUpdate();
			String result = (isWhitesTurn() ? "White" : "Black") + " has won the game! Returning to the main menu.";
			GUIFunctionality.sendWarning(result, this);
		}

		this.dispose();
	}
}
