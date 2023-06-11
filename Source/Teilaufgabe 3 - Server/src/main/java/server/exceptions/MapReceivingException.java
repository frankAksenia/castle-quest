package server.exceptions;

public class MapReceivingException extends MapValidationException {

	private static final long serialVersionUID = 1L;

	public MapReceivingException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
