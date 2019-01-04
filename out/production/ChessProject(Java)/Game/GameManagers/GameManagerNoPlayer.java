package Game.GameManagers;

import Func.*;
import Game.Players.*;
import Game.Pieces.*;
import java.awt.Point;

public class GameManagerNoPlayer extends GameManager {

	Player p1 = new God();

	@Override
	public void actionPerformed(String actionCode) {
		PieceColor color = color(isWhitesTurn());
		Tuple<Point, Point> move = p1.determineMove(boardManager.cloneBoard(), boardManager, color);
		boolean success = interpretMove(move.x, move.y);
	}

	@Override
	public void redrawBoard() {
		gameGuiManager.redrawBoard(boardManager.cloneBoard(), true,
				boardManager.getLightPoints(), boardManager.getDarkPoints());
	}

}
