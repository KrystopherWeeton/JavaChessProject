package Game.GameManagers;

import Func.*;
import Game.Pieces.*;

import java.awt.*;
import java.util.logging.*;

public class GameTesterNoPlayer extends GameManagerNoPlayer {

	private final boolean displayFinalResults;

	public GameTesterNoPlayer(boolean displayFinalResults) {
		super(displayFinalResults);
		this.displayFinalResults = displayFinalResults;
		if (!displayFinalResults) {
			this.setVisible(false);
			gameGuiManager.setVisible(false);
		}
	}


	@Override
	protected void handleGameOver() {

		if (displayFinalResults) {
			printGameOverMessage();
		}

		this.dispose();
	}

	@Override
	protected void handleBoardUpdate() {
		if (displayFinalResults)
			super.handleBoardUpdate();
	}
}
