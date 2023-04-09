package exceptions;

public class PlayerRegistrationException extends NetworkException {

	private static final long serialVersionUID = 1L;
	
	private final String playerID;
	
	public PlayerRegistrationException(String exceptionName, String exceptionMessage, String playerID) {
		super(exceptionName, exceptionMessage);
		this.playerID = playerID;
	}
	
	public String getPlayerID() {
		return this.playerID;
	}

}
