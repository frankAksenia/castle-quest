package exceptions;

public class RequestStateException extends NetworkException {
	
	private static final long serialVersionUID = 1L;
				
	public RequestStateException(String exceptionName, String exceptionMessage) {
		super(exceptionName, exceptionMessage);
	}
}
