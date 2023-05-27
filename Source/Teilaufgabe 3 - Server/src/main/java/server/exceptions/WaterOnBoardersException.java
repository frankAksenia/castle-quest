package server.exceptions;

public class WaterOnBoardersException extends MapValidationException {

	private static final long serialVersionUID = 1L;

	public WaterOnBoardersException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
