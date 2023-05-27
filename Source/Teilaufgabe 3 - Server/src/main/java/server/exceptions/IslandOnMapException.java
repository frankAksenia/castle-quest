package server.exceptions;

public class IslandOnMapException extends MapValidationException {

	private static final long serialVersionUID = 1L;

	public IslandOnMapException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
