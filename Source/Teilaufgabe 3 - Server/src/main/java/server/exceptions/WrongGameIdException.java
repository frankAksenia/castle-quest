package server.exceptions;

public class WrongGameIdException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public WrongGameIdException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
