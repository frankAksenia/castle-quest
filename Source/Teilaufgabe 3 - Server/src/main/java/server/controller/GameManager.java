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
	
	private final GameManagerService gameManagerService;

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
}
