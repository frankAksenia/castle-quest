package server.services;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import server.exceptions.ActionNotInTurnException;
import server.model.GameData;
import server.model.GameId;
import server.model.GamePlayer;
import server.model.GameRepository;
import server.model.PlayerId;

public class GameManagerServiceTest {

	@Test
	public void canHaveMoreGames_addNewGame_gameIsAdded() {
		
		// Arrange 
		GameManagerService gameManagerService = new GameManagerService(new GameRepository());
		GameId gameId = new GameId("xxxxx"); 
		GameData gameData = new GameData();
		
		// Act
		gameManagerService.addNewGame(gameId, gameData);
		
		// Assert 
        Map<GameId, GameData> runningGames = gameManagerService.getAllRunningGames();
        Assertions.assertEquals(1, runningGames.size());
        Assertions.assertTrue(runningGames.containsKey(gameId));
        Assertions.assertEquals(gameData, runningGames.get(gameId));
	}
	

	@Test
    public void haveThreeGames_removeTwoGames_twoGamesAreRemoved() {
		
		// Arrange
		GameManagerService gameManagerService = new GameManagerService(new GameRepository());
        GameId gameId1 = new GameId("xxxxx");
        GameData gameData1 = new GameData();

        GameId gameId2 = new GameId("yyyyy");
        GameData gameData2 = new GameData();

        GameId gameId3 = new GameId("zzzzz");
        GameData gameData3 = new GameData();

        gameManagerService.addNewGame(gameId1, gameData1);
        gameManagerService.addNewGame(gameId2, gameData2);
        gameManagerService.addNewGame(gameId3, gameData3);

        // Act 
        gameManagerService.removeOldestGames(2);
        Map<GameId, GameData> runningGames = gameManagerService.getAllRunningGames();

        
        // Assert
        Assertions.assertEquals(1, runningGames.size());
        Assertions.assertTrue(runningGames.containsKey(gameId3));
        Assertions.assertFalse(runningGames.containsKey(gameId1));
        Assertions.assertFalse(runningGames.containsKey(gameId2));
    }
	
	@Test
	public void turnOfCorrectClient_clientSendsAction_actionIsAccepted() {
		
		// Arrange 
		GameManagerService gameManagerService = new GameManagerService(new GameRepository());
		GamePlayer correctPlayer = new GamePlayer(new PlayerId("xxxxx"), "Max", "Mustermann", "maxMust", false);
		GamePlayer wrongPlayer = new GamePlayer(new PlayerId("yyyyy"), "Maxin", "Musterfrau", "maxinMust", false);
		GameData gameData = new GameData();
		gameData.addPlayer(correctPlayer);
		gameData.addPlayer(wrongPlayer);
		gameData.setCurrentPlayer(correctPlayer.playerId());
		
		// Act
		boolean result = gameManagerService.verifyActionSentInTurn(correctPlayer.playerId());

		// Assert
		Assertions.assertTrue(result);
	}
	
	@Test
	public void turnOfWrongClient_clientSendsAction_errorIsThrown() {
		
		// Arrange 
		GameManagerService gameManagerService = new GameManagerService(new GameRepository());
		GamePlayer correctPlayer = new GamePlayer(new PlayerId("xxxxx"), "Max", "Mustermann", "maxMust", false);
		GamePlayer wrongPlayer = new GamePlayer(new PlayerId("yyyyy"), "Maxin", "Musterfrau", "maxinMust", false);
		GameData gameData = new GameData();
		gameData.addPlayer(correctPlayer);
		gameData.addPlayer(wrongPlayer);
		gameData.setCurrentPlayer(correctPlayer.playerId());
	
		// Act and Assert
		Assertions.assertThrows(ActionNotInTurnException.class, () -> {
            gameManagerService.verifyActionSentInTurn(wrongPlayer.playerId());
        });
	}
}
