package Game.GameManagers;

import Func.*;

import java.awt.*;
import java.util.logging.*;

public class GameManagerOnePlayer extends GameManager {

	private static Logger logger = Logger.getLogger("GameManagerOnePlayer");
	private static final boolean LOGGING = Consts.LOGGING;

	@Override
	public void actionPerformed(String actionCode) {
		if (LOGGING)
			logger.warning("Functionality of actionPerformed in GameManagerOnePlayer is unsupported");
	}

	@Override
	public void redrawBoard() {
		gameGuiManager.redrawBoard(boardManager.cloneBoard(), true,
				boardManager.getLightPoints(), boardManager.getDarkPoints());
	}

	@Override
	public void promotePawn(Point at) {
		if (LOGGING)
			logger.warning("Functionality of promotePawn in GameMAnagerOnePlayer is unsupported");
	}
}
