package exceptions;

public abstract class NetworkException extends RuntimeException {

	private static final long serialVersionUID = 1L;
		
	private final String exceptionName;
	
	NetworkException(String exceptionName, String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionName = exceptionName;
	}
	
	public String getExceptionName() {
		return this.exceptionName;
	}

}
