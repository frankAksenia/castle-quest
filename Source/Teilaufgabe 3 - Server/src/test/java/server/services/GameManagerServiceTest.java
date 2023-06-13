package server.services;

import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import server.model.GameData;
import server.model.GameId;
import server.model.GameRepository;

public class GameManagerServiceTest {
	
	private GameRepository gameRepository = new GameRepository();

	@Test
	public void canHaveMoreGames_addNewGame_gameIsAdded() {
		
		// Arrange 
		GameManagerService gameManagerService = new GameManagerService(gameRepository);
		GameId gameId = new GameId("xxxxx"); 
		GameData gameData = new GameData();
		
		// Act
		gameManagerService.addNewGame(gameId, gameData);
		
		// Assert 
        Map<GameId, GameData> runningGames = gameRepository.getAllRunningGames();
        Assertions.assertEquals(1, runningGames.size());
        Assertions.assertTrue(runningGames.containsKey(gameId));
        Assertions.assertEquals(gameData, runningGames.get(gameId));
	}
	

	@Test
    public void haveThreeGames_removeTwoGames_twoGamesAreRemoved() {
		
		// Arrange
		GameManagerService gameManagerService = new GameManagerService(gameRepository);
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
        gameRepository.removeOldestGames(2);
        Map<GameId, GameData> runningGames = gameRepository.getAllRunningGames();

        
        // Assert
        Assertions.assertEquals(1, runningGames.size());
        Assertions.assertTrue(runningGames.containsKey(gameId3));
        Assertions.assertFalse(runningGames.containsKey(gameId1));
        Assertions.assertFalse(runningGames.containsKey(gameId2));
    }
}
