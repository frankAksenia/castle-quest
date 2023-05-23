package server.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

import messagesbase.ResponseEnvelope;
import messagesbase.UniqueGameIdentifier;
import messagesbase.UniquePlayerIdentifier;
import messagesbase.messagesfromclient.PlayerHalfMap;
import messagesbase.messagesfromclient.PlayerRegistration;
import messagesbase.messagesfromserver.GameState;
import server.exceptions.PlayerRegistrationException;
import server.model.GameData;
import server.model.GameId;
import server.services.GameIdGeneratorService;

// Controller 
public class GameManager {
	
	/* Using LinkedHashMap to preserve order of added elements
	 * to know which games are the oldest and can be removed if Server is overloaded
	 * */
	private Map<GameId, GameData> runningGames = new LinkedHashMap<>();

	public Map<GameId, GameData> getRunningGames() {
		return Collections.unmodifiableMap(runningGames);
	}

	public void addNewGame(GameId gameId, GameData gameData) {
		runningGames.put(gameId, gameData);
	}
	
	public void removeOldGames(int amountOfGamesToRemove) {
		Iterator<Map.Entry<GameId, GameData>> mapIterator = this.getRunningGames().entrySet().iterator();
        int count = 0;
        while (mapIterator.hasNext() && count < amountOfGamesToRemove) {
            mapIterator.next();
            mapIterator.remove();
            ++count;
        }
	}
	
	public UniqueGameIdentifier processGameCreation() {
		GameIdGeneratorService gameIdGenerator = new GameIdGeneratorService();
		GameId id = gameIdGenerator.generateRandomID();
		return new UniqueGameIdentifier(id.id());
	}
	
	public ResponseEnvelope<UniquePlayerIdentifier> processPlayerRegistration(PlayerRegistration playerRegistration) {
		UniquePlayerIdentifier newPlayerID = new UniquePlayerIdentifier(UUID.randomUUID().toString());
		
		if(playerRegistration.getStudentFirstName().isBlank()) 
			throw new PlayerRegistrationException("First name missing", "Required first name of a player is not provided");
		
		if(playerRegistration.getStudentLastName().isBlank()) 
			throw new PlayerRegistrationException("Last name missing", "Required last name of a player is not provided");
		
		if(playerRegistration.getStudentUAccount().isBlank()) 
			throw new PlayerRegistrationException("UAccount missing", "Required uaccount of a player is not provided");
		

		ResponseEnvelope<UniquePlayerIdentifier> playerIDMessage = new ResponseEnvelope<>(newPlayerID);
		return playerIDMessage;
	}
	
	public ResponseEnvelope<?> processPlayerHalfmap(UniqueGameIdentifier gameID, PlayerHalfMap playerHalfMap) {
		return new ResponseEnvelope<>();
	}
	
	public ResponseEnvelope<GameState> processGameStateRequest(UniqueGameIdentifier gameID, UniquePlayerIdentifier playerID) {
		ResponseEnvelope<GameState> gameState = new ResponseEnvelope<>(new GameState());
		return gameState;
	}
	
	

}
