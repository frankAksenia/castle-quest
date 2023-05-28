package server.controller;

import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.UniqueGameIdentifier;
import server.converters.ServerClientConverter;
import server.model.GameData;
import server.model.GameId;
import server.services.GameIdGeneratorService;
import server.services.GameManagerService;

@RestController 
public class GameManager {
	
	private final GameManagerService gameManagerService;
	private final GameIdGeneratorService gameIdGeneratorService;
	private final ServerClientConverter serverClientConverter;

	@Autowired
	public GameManager(GameManagerService gameManagerService, GameIdGeneratorService gameIdGeneratorService, ServerClientConverter serverClientConverter) {
		this.gameManagerService = gameManagerService;
		this.gameIdGeneratorService = gameIdGeneratorService;
		this.serverClientConverter = serverClientConverter;
	} 
	
	public UniqueGameIdentifier processGameCreation() {
		GameId gameId = gameIdGeneratorService.generateRandomID();
		gameManagerService.addNewGame(gameId, new GameData());
		return this.serverClientConverter.convertGameId(gameId);
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
}
