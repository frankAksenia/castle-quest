package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import server.exceptions.WrongPlayerIdException;
import server.model.GameData;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.GameRepository;
import server.model.PlayerId;

public class PlayerIdVerificationServiceTest {
	
	private static GameRepository gameRepository;
	private static PlayerIdVerificationSerivce playerIdVerificationService;
	private static GameId gameId;
	private static GameData gameData;
	private static GamePlayer checkPlayer;
	
	@BeforeAll
	public static void initialize() {
		// Arrange
		gameRepository = new GameRepository();
		playerIdVerificationService = new PlayerIdVerificationSerivce(gameRepository);
		gameId = new GameId("xxxxx");
		gameData = new GameData();
		gameRepository.addNewGame(gameId, gameData);
		checkPlayer = new GamePlayer(new PlayerId("yyyyy"), "Max", "Mustermann", "maxMust", false);
		gameData.addPlayer(checkPlayer);
	}
	
	@Test
	public void receivedRequest_checkingCorrectPlayerId_playerIdIsAccepted() {
		// Act 
		boolean result = playerIdVerificationService.verifyPlayerId(gameId, checkPlayer.playerId());
		// Assert
		Assertions.assertTrue(result);
	}
	
	@Test
	public void receivedRequest_checkingWrongPlayerId_throwsException() {
		// Act and Assert
		Assertions.assertThrows(WrongPlayerIdException.class, () -> {
            playerIdVerificationService.verifyPlayerId(gameId, new PlayerId("ranid"));
        });
	}
}
