package Game.Players;

import java.awt.Point;
import Func.Tuple;
import Game.Pieces.Piece;
import Game.BoardManager;
import Game.Pieces.PieceColor;

abstract public class Player {

	abstract public Tuple<Point, Point> determineMove(Piece[][] board, BoardManager manager, PieceColor color);

}
