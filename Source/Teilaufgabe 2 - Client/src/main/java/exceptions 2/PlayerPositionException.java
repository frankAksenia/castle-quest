package exceptions;

public class PlayerPositionException extends GameMapException {
	
private static final long serialVersionUID = 1L;
	
	public PlayerPositionException(String exceptionName, String exceptionMessage) {
		super(exceptionName, exceptionMessage);
	}
}
