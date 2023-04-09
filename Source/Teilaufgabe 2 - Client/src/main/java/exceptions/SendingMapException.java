package exceptions;

public class SendingMapException extends NetworkException {
	
	private static final long serialVersionUID = 1L;
	
	private final String playerID;
	
	public SendingMapException(String exceptionName, String exceptionMessage, String playerID) {
		super(exceptionName, exceptionMessage);
		this.playerID = playerID;
	}
	
	public String getPlayerID() {
		return this.playerID;
	}
}
