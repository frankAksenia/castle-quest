package server.converters;

import messagesbase.ResponseEnvelope;
import messagesbase.UniquePlayerIdentifier;
import server.model.PlayerId;

public class ClientServerConverter {
	
	public PlayerId convertPlayerId(ResponseEnvelope<UniquePlayerIdentifier> response) {
		PlayerId playerID = new PlayerId(response.getData().get().toString());
		return playerID;
	}
	
}
