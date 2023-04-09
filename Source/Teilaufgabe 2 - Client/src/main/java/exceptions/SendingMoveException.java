package exceptions;

public class SendingMoveException extends NetworkException {

	private static final long serialVersionUID = 1L;
	
	public SendingMoveException(String exceptionName, String exceptionMessage) {
		super(exceptionName, exceptionMessage);
	}
}
