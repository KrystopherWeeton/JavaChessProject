package Game.GameManagers;

import Func.*;
import Game.Players.*;
import Game.Pieces.*;
import java.awt.Point;

public class GameManagerNoPlayer extends GameManager {

	Player p1 = new God();

	public GameManagerNoPlayer(boolean isVisible) {
		super(isVisible);
	}

	public GameManagerNoPlayer() {
		this(true);
	}

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

	@Override
	public boolean interpretMove(Point from, Point to) {
		boolean returnValue = super.interpretMove(from, to);

		// Checks if we are in the possibility of an infinite repetition.
		if (boardManager.inInfiniteGame()) {
			handleGameOver(true);		// End the game
		}
		return returnValue;
	}

	@Override
	public void promotePawn(Point at) {
		int choice = MiscFunc.getRandom(0, 1);
		boardManager.promotePawn(at, choice);
	}

}
