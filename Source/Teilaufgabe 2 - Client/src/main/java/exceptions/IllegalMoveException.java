package exceptions;

public class IllegalMoveException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	private String exceptionName;
			
	public IllegalMoveException(String exceptionName, String exceptionMessage) {
		super(exceptionMessage);
		this.exceptionName = exceptionName;
	}
	
	public String getExceptionName() {
		return this.exceptionName;
	}
}
