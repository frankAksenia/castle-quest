package server.exceptions;

public class AmountOfPlayersException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public AmountOfPlayersException(String exceptionMessage) {
		super(exceptionMessage);
	}
}
