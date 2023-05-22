package server.exceptions;

public class PlayerRegistrationException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public PlayerRegistrationException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
