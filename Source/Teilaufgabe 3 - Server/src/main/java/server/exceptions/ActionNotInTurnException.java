package server.exceptions;

public class ActionNotInTurnException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public ActionNotInTurnException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
