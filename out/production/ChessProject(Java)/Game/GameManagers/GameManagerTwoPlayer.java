package Game.GameManagers;

import java.awt.*;

public class GameManagerTwoPlayer extends GameManager{

	protected Point selectedPoint = null;
	protected Point selectedDisplayPoint = null;

	public GameManagerTwoPlayer() {
		super(true);
	}

	/*
	Used to get input from the user when clicking on the board itself. The
	input is then translated, and sent to the board to be handled.
	 */
	@Override
	public void actionPerformed(String actionCode) {
		Point p = translateString(actionCode);
		if (p == null) {
			System.err.println("There was an error translating the actionCode in actionPerformed");
			return;
		}
		if (selectedPoint == null) {	// we did not have a previously selected point
			selectedPoint = p;					// start tracking the selected point
			// this is the display square which will be highlighted, as if it is black's turn
			// then (x,y) is displayed at point (7 - x, 7 - y)
			selectedDisplayPoint = (isWhitesTurn()) ? (Point)p.clone() : new Point(7 - p.x, 7 - p.y);
			gameGuiManager.selectSquare(selectedDisplayPoint);
		} else {						// we had a previously selected point
			boolean success = interpretMove(selectedPoint, p);	// handle the possible move from selectedPoint to p
			gameGuiManager.deselectSquare(selectedDisplayPoint);	// deselects the selected point
			selectedPoint = null;				// deselect the old point
		}
	}

	@Override
	public void redrawBoard() {
		gameGuiManager.redrawBoard(boardManager.cloneBoard(), isWhitesTurn(),
				boardManager.getLightPoints(), boardManager.getDarkPoints());
	}

	@Override
	public void promotePawn(Point at) {
		boardManager.promotePawn(at);
	}


}