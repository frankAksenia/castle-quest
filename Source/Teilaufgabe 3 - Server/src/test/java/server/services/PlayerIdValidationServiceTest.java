package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import server.exceptions.AmountOfPlayersException;
import server.model.GameData;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.GameRepository;
import server.model.PlayerId;

public class PlayerIdValidationServiceTest {
	
	private static GameRepository gameRepository;
	private static PlayerIdValidationService playerIdVerificationService;
	private static GameId gameId;
	private static GameData gameData;
	private static GamePlayer checkPlayer;
	
	@BeforeAll
	public static void initialize() throws AmountOfPlayersException {
		// Arrange
		gameRepository = new GameRepository();
		playerIdVerificationService = new PlayerIdValidationService(gameRepository);
		gameId = new GameId("xxxxx");
		gameData = new GameData();
		gameRepository.addNewGame(gameId, gameData);
		checkPlayer = new GamePlayer(new PlayerId("yyyyy"), "Max", "Mustermann", "maxMust");
		gameData = gameRepository.getRunningGameById(gameId);
		gameData.addPlayer(checkPlayer);
	}
	
	@Test
	public void receivedRequest_checkingCorrectPlayerId_playerIdIsAccepted() {
		// Act 
		boolean result = playerIdVerificationService.validatePlayerId(gameId, checkPlayer.playerId());
		// Assert
		Assertions.assertFalse(result);
	}
	
	@Test
	public void receivedRequest_checkingWrongPlayerId_throwsException() {
		// Act 
		boolean result = playerIdVerificationService.validatePlayerId(gameId, new PlayerId("xxxxx"));
		// Assert
		Assertions.assertTrue(result);
	}
}
