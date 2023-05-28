package server.converters;

import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import server.model.GameId;

@Component
public class ServerClientConverter {
	
	public UniqueGameIdentifier convertGameId(GameId sendGameId) {
		return new UniqueGameIdentifier(sendGameId.id());
	}

}
