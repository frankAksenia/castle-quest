package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import server.model.GameId;

public class GameIdGeneratorServiceTest {

	@Test
	public void noGameIdGenerated_generateRandomGameId_lengthIsCorrect() {
		
		// Arrange
		GameIdGeneratorService gameIdGeneratorService = new GameIdGeneratorService();
		int expectedLength = 5;
		
		// Act
		GameId gameId = gameIdGeneratorService.generateRandomID();
		
		// Assert
		Assertions.assertEquals(expectedLength, gameId.id().length());
	}
	
	@RepeatedTest(5)
	public void noGameIdGenerated_generateRandomGameId_noNumbers() {
		// Arrange
		GameIdGeneratorService gameIdGeneratorService = new GameIdGeneratorService();
		String regExIntegerSubstring = ".*\\d.*";
				
		// Act
		GameId gameId = gameIdGeneratorService.generateRandomID();
				
		// Assert
		Assertions.assertTrue(!gameId.id().matches(regExIntegerSubstring));
	}
}
