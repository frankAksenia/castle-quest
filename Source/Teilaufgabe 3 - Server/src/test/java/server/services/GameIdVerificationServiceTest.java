package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import server.model.GameData;
import server.model.GameId;
import server.model.GameRepository;

public class GameIdVerificationServiceTest {

	@Test
	public void receivedGameId_gameIdIsCorrect_returnsTrue() {
		
		// Arrange
		GameIdVerificationService gameIdVerificationService = new GameIdVerificationService();
		GameRepository gameRepository = new GameRepository();
		GameId existingId = new GameId("xxxxx");
		gameRepository.addNewGame(existingId, new GameData());
		boolean expected = true;
		
		// Act
		boolean result = gameIdVerificationService.verifyGameId(existingId);
		
		// Assert 
		Assertions.assertEquals(expected, result);
	}
	
	@Test
	public void receivedGameId_gameIdIsWrong_returnFalse() {
		// Arrange
		GameIdVerificationService gameIdVerificationService = new GameIdVerificationService();
		GameRepository gameRepository = new GameRepository();
		GameId existingId = new GameId("xxxxx");
		GameId notExistingId = new GameId("yyyyy");
		gameRepository.addNewGame(existingId, new GameData());
		boolean expected = false;
				
		// Act
		boolean result = gameIdVerificationService.verifyGameId(notExistingId);
				
		// Assert 
		Assertions.assertEquals(expected, result);
	}
}
