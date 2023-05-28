package server.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import server.exceptions.WrongPlayerIdException;
import server.model.GameData;
import server.model.GamePlayer;
import server.model.PlayerId;

public class PlayerIdVerificationServiceTest {

	private PlayerIdVerificationSerivce playerIdVerificationService = new PlayerIdVerificationSerivce();
	
	@Test
	public void receivedRequest_checkingCorrectPlayerId_playerIdIsAccepted() {
		
		// Arrange
		GameData gameData = new GameData();
		GamePlayer checkPlayer = new GamePlayer(new PlayerId("yyyyy"), "Max", "Mustermann", "maxMust", false);
		gameData.setFirstPlayer(checkPlayer);

		// Act 
		boolean result = playerIdVerificationService.verifyPlayerId(checkPlayer.playerId());
		
		// Assert
		Assertions.assertTrue(result);
	}
	
	@Test
	public void receivedRequest_checkingWrongPlayerId_throwsException() {
		
		// Arrange
		GameData gameData = new GameData();
		GamePlayer checkPlayer = new GamePlayer(new PlayerId("yyyyy"), "Max", "Mustermann", "maxMust", false);
		gameData.setFirstPlayer(checkPlayer);

		// Act and Assert
		Assertions.assertThrows(WrongPlayerIdException.class, () -> {
            playerIdVerificationService.verifyPlayerId(new PlayerId("ranid"));
        });
	}
}
