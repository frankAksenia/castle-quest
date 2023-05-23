package server.exceptions;

/*
 * Generic map validation exception.
 */
public class MapValidationException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public MapValidationException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}
}
