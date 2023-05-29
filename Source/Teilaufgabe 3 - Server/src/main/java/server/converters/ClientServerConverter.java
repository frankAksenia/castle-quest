package server.converters;

import org.springframework.stereotype.Component;

import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerRegistration;
import server.model.GameId;
import server.model.GamePlayer;
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
	
	public GamePlayer convertPlayerRegistration(PlayerRegistration receivedRegistration, PlayerId playerId) {
		String firstName = receivedRegistration.getStudentFirstName();
		String lastName = receivedRegistration.getStudentLastName();
		String uaccount = receivedRegistration.getStudentUAccount();
		GamePlayer gamePlayer = new GamePlayer(playerId, firstName, lastName, uaccount, false);
		return gamePlayer;
	}
	
}
