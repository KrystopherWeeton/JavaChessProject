package Game.Players;

import Func.*;
import Game.*;
import Game.Pieces.*;
import java.awt.Point;
import Game.Pieces.PieceColor;
import java.util.ArrayList;
import java.util.logging.*;

public class God extends Player{

	PieceColor color;
	boolean PieceColorSet = false;

	private double threatWeight;
	private double defenseWeight;
	private double takeWeight;

	int phase = 0;

	private static final Logger logger = Logger.getLogger("God");
	private static final boolean LOGGING = Consts.LOGGING;


	/*
	0: Base behaviour for now (universal)
	 */

	public God(double threatWeight, double defenseWeight, double takeWeight) {
		double total = threatWeight + defenseWeight + takeWeight;
		this.threatWeight = threatWeight / total;
		this.defenseWeight = defenseWeight / total;
		this.takeWeight = takeWeight / total;
	}

	@Override
	public Tuple<Point, Point> determineMove(Piece[][] board, BoardManager manager, PieceColor color) {

		// empties out the move container
		move = null;
		boolean done;
		switch(phase) {
			case 0:
				done = baseBehaviour(manager, color);
				break;
			default:
				if (LOGGING) {
					logger.warning("Fell out of phases for computer player");
				}
				move = getRandomMove(manager, color);
				done = true;

		}
		if (done) phase++;

		return move;
	}

	Tuple<Point, Point> move;

	private boolean baseBehaviour(BoardManager manager, PieceColor color) {
		ArrayList<Tuple<Point, Point>> moves = manager.findAllMoves(color);
		double maximum = -1;
		for (Tuple<Point, Point> t : moves) {
			if (!manager.canMove(t.x, t.y)) continue;
			double score = scoreMove(t, manager, color);
			if (score > maximum) {
				maximum = score;
				move = t;
			}
		}
		return false;
	}

	boolean moveTies = false;

	private Tuple<Point, Point> getRandomMove(BoardManager manager, PieceColor color) {
		ArrayList<Tuple<Point, Point>> list = manager.findAllMoves(color);
		Tuple<Point, Point> move = list.get(MiscFunc.getRandom(0, list.size() - 1));
		while (!manager.isValidMove(move.x, move.y) || !manager.isLegalMove(move.x, move.y))
			move = list.get(MiscFunc.getRandom(0, list.size() - 1));
		return move;
	}

	/*
	 Determines a numerical score for a potential move on the board given.
	 @parameters
	 	move: a Tuple representing the move
	 	board: the current board state
	 	BoardManager: the current BoardManager
	 	PieceColor: the piece color for this player
	 @return: a double which represents the quality of the move
	*/
	private double scoreMove(Tuple<Point, Point> move, BoardManager manager, PieceColor color) {

		Function f = new Function() {

			double threatScore = 0;
			double defenseScore = 0;

			@Override
			public boolean run(int x, int y) {
				threatScore = scoreThreats(manager, color);
				defenseScore = scoreDefense(manager, color);
				setScores(threatScore, defenseScore);
				return false;
			}
		};

		manager.testMove(move.x, move.y, f);

		takeScore = scoreTaking(manager, color, move.x, move.y);

		if (threatScore == -1 || defenseScore == -1) {
			if (LOGGING) {
				logger.warning("Unable to calculate threatScore and defenseScore within player");
			}
			return 0;
		}

		double ret = threatScore * threatWeight + defenseScore * defenseWeight + takeWeight + takeScore;

		threatScore = -1;
		defenseScore = -1;
		takeScore = -1;

		return ret;
	}

	double threatScore = -1;
	double defenseScore = -1;
	double takeScore = -1;

	private void setScores(double threatScore, double defenseScore) {
		this.threatScore = threatScore;
		this.defenseScore = defenseScore;
	}

	/*
	Scores the threats of the current position
	@parameters
		manager: the board manager for this player
		color: the color for this player
	@return: a double which represents the estimated quality of the threats in this position
	 */
	private double scoreThreats(BoardManager manager, PieceColor color) {
		ArrayList<Tuple<Point, Point>> takes = manager.determineThreats(color);

		int sum = 0;
		for (Tuple<Point, Point> t : takes) {
			ConstPiece p = manager.getPiece(t.y);
			sum += p.getValue();
		}

		int totalPoints = (color == PieceColor.light ? manager.remainingLightPoints() : manager.remainingDarkPoints());

		return (double)sum / (double)totalPoints;

	}

	/*
	Scores the defense of the current position
	@parameters
		manager: the board manager for this player
		color: the color for this player
	@return: a double representing the quality of the defense in this position
	 */
	private double scoreDefense(BoardManager manager, PieceColor color) {
		PieceColor other = (color == PieceColor.light) ? PieceColor.dark : PieceColor.light;
		return scoreThreats(manager, other);
	}

	/*
	Scores the value from taking the given pieces
	@parameters
		manager: the board manager for this player
		color: the color for this player
	@return: a double representing the quality fo the defense in this position
	 */
	private double scoreTaking(BoardManager manager, PieceColor color, Point from, Point to) {
		ConstPiece p1 = manager.getPiece(from);
		ConstPiece p2 = manager.getPiece(to);
		if (p2 == null || !p1.colorMatches(color) || p2.colorMatches(color)) return 0;

		double diff = p1.getValue() - p2.getValue();
		if (diff == 0) return 0;
		return (diff + 8) / (double)16;

	}

}
