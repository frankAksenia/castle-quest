package exceptions;

import clientData.PlayerId;

public class SendingMapException extends NetworkException {
	
	private static final long serialVersionUID = 1L;
	
	private final PlayerId playerID;
	
	public SendingMapException(String exceptionName, String exceptionMessage, PlayerId playerID) {
		super(exceptionName, exceptionMessage);
		this.playerID = playerID;
	}
	
	public PlayerId getPlayerID() {
		return this.playerID;
	}
}
