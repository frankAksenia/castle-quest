package server.exceptions;

public class WrongPlayerIdException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public WrongPlayerIdException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}
}
