package Game.GameManagers;

import Func.*;

public class GameManagerOnePlayer extends GameManager {

	@Override
	public void actionPerformed(String actionCode) {
		GUIFunctionality.sendWarning("FUCK YOU", this);
	}

	@Override
	public void redrawBoard() {
		gameGuiManager.redrawBoard(boardManager.cloneBoard(), true,
				boardManager.getLightPoints(), boardManager.getDarkPoints());
	}
}
