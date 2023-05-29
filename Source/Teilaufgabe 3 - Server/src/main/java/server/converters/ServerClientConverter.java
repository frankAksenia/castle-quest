package server.converters;

import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import server.model.GameId;
import server.model.PlayerId;

@Component
public class ServerClientConverter {
	
	public UniqueGameIdentifier convertGameId(GameId sendGameId) {
		return new UniqueGameIdentifier(sendGameId.id());
	}
	
	public UniquePlayerIdentifier convertPlayerId(PlayerId sendPlayerId) {
		return new UniquePlayerIdentifier(sendPlayerId.id());
	}

}
