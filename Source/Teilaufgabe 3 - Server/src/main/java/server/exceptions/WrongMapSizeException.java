package server.exceptions;

public class WrongMapSizeException extends MapValidationException {

	private static final long serialVersionUID = 1L;

	public WrongMapSizeException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}
}
