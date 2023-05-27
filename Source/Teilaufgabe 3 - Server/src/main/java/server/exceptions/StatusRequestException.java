package server.exceptions;

public class StatusRequestException extends GenericExampleException {

	private static final long serialVersionUID = 1L;

	public StatusRequestException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
