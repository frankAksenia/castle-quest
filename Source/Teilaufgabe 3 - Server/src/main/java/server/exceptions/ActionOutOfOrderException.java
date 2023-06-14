package server.exceptions;

public class ActionOutOfOrderException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public ActionOutOfOrderException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
