package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import server.exceptions.WrongGameIdException;
import server.model.GameData;
import server.model.GameId;
import server.model.GameRepository;

public class GameIdValidationServiceTest {
	
	private static GameRepository gameRepository;
	private static GameIdValidationService gameIdVerificationService;
	private static GameId existingId;
	
	@BeforeAll
	public static void initialize() {
		gameRepository = new GameRepository();
		gameIdVerificationService = new GameIdValidationService(gameRepository);
		existingId = new GameId("xxxxx");
	}

	@Test
	public void receivedGameId_gameIdIsCorrect_returnsTrue() {
		// Arrange
		gameRepository.addNewGame(existingId, new GameData());
		
		// Act
		gameIdVerificationService.validateGameId(existingId);
		
		// Assert 
		Assertions.assertDoesNotThrow(() -> {
            gameIdVerificationService.validateGameId(existingId);
        });
	}
	
	@Test
	public void receivedGameId_gameIdIsWrong_throwsException() {
		// Arrange
		GameId notExistingId = new GameId("yyyyy");
		gameRepository.addNewGame(existingId, new GameData());
				
		// Act and Assert
		Assertions.assertThrows(WrongGameIdException.class, () -> {
            gameIdVerificationService.validateGameId(notExistingId);
        });
	}
}
