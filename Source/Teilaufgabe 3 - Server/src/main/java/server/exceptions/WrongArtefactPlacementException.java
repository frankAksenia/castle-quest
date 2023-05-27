package server.exceptions;

public class WrongArtefactPlacementException extends MapValidationException {

	private static final long serialVersionUID = 1L;

	public WrongArtefactPlacementException(String errorName, String errorMessage) {
		super(errorName, errorMessage);
	}

}
