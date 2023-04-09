package exceptions;

public class GameMapException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	private final String exceptionName;
	
	GameMapException(String exceptionName, String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionName = exceptionName;
	}
	
	public String getExceptionName() {
		return this.exceptionName;
	}
}
