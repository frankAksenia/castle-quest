package server.exceptions;

public class WrongTerrainCountException extends MapValidationException {

	private static final long serialVersionUID = 1L;

	public WrongTerrainCountException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}
	
}