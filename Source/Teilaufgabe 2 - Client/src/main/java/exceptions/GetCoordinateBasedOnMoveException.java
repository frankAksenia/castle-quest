package exceptions;

public class GetCoordinateBasedOnMoveException extends GameMapException {
	
private static final long serialVersionUID = 1L;
	
	public GetCoordinateBasedOnMoveException(String exceptionName, String exceptionMessage) {
		super(exceptionName, exceptionMessage);
	}
}

