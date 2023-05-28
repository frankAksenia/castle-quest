package server.converters;

import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import server.model.GameId;
import server.model.PlayerId;

@Component
public class ClientServerConverter {
	
	public PlayerId convertPlayerId(UniquePlayerIdentifier receivedId) {
		PlayerId playerId = new PlayerId(receivedId.getUniquePlayerID());
		return playerId;
	}
	
	public GameId convertGameId(UniqueGameIdentifier receivedId) {
		GameId gameId = new GameId(receivedId.getUniqueGameID());
		return gameId;
	}
	
}
