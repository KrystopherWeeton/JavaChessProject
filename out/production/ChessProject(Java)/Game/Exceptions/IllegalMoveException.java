package Game.Exceptions;

public class IllegalMoveException extends RuntimeException {
	public IllegalMoveException(String errorMessage) {
		super(errorMessage);
	}
}