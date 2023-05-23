package server.controller;

import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

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
import server.services.GameManagerService;

@RestController 
public class GameManager {
	
	private GameManagerService gameManagerService;

	@Autowired
	public GameManager(GameManagerService gameManagerService) {
		this.gameManagerService = gameManagerService;
	} 

	public Map<GameId, GameData> getAllRunningGames() {
		return this.gameManagerService.getAllRunningGames();
	}

	public void addNewGame(GameId gameId, GameData gameData) {
	}
	
	public void removeOldGames(int amountOfGamesToRemove) {
		Iterator<Map.Entry<GameId, GameData>> mapIterator = this.getAllRunningGames().entrySet().iterator();
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
