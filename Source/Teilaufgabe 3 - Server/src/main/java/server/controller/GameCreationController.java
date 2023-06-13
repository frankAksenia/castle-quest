package server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import messagesbase.UniqueGameIdentifier;
import server.converters.ServerClientConverter;
import server.model.GameData;
import server.model.GameId;
import server.services.GameIdGeneratorService;
import server.services.GameManagerService;

@RestController 
public class GameCreationController {
	
	private final GameManagerService gameManagerService;
	private final GameIdGeneratorService gameIdGeneratorService;
	private final ServerClientConverter serverClientConverter;

	@Autowired
	public GameCreationController(GameManagerService gameManagerService, GameIdGeneratorService gameIdGeneratorService, ServerClientConverter serverClientConverter) {
		this.gameManagerService = gameManagerService;
		this.gameIdGeneratorService = gameIdGeneratorService;
		this.serverClientConverter = serverClientConverter;
	} 
	
	public UniqueGameIdentifier processGameCreation() {
		GameId gameId = gameIdGeneratorService.generateRandomID();
		this.addNewGame(gameId);
		return this.serverClientConverter.convertGameId(gameId);
	}

	private void addNewGame(GameId gameId) {
		gameManagerService.addNewGame(gameId, new GameData());
	}
}
