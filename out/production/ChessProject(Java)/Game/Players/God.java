package Game.Players;

import Func.*;
import Game.*;
import Game.Pieces.Piece;
import java.awt.Point;
import Game.Pieces.PieceColor;
import java.util.ArrayList;

public class God extends Player{

	@Override
	public Tuple<Point, Point> determineMove(Piece[][] board, BoardManager manager, PieceColor color) {
		ArrayList<Tuple<Point, Point>> list = manager.findAllMoves(color);
		Tuple<Point, Point> move = list.get(MiscFunc.getRandom(0, list.size() - 1));
		while (!manager.isValidMove(move.x, move.y) || !manager.isLegalMove(move.x, move.y))
			move = list.get(MiscFunc.getRandom(0, list.size() - 1));
		return move;
	}
}
