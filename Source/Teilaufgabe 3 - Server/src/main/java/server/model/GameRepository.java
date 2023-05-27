package server.model;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

	/* Using LinkedHashMap to preserve order of added elements
	 * to know which games are the oldest and can be removed if Server is overloaded
	 * */
	
	private final int MAX_GAMES = 99;
	
	private Map<GameId, GameData> runningGames;
	
	public GameRepository() {
		this.runningGames = new LinkedHashMap<GameId, GameData>();
	}
	
	public Map<GameId,GameData> getAllRunningGames() {
		return this.runningGames;
	}
	
	public GameData getRunningGameById(GameId gameId) {
		return new GameData();
	}
	
	public int getAmountOfActiveGames() {
		return 0;
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {
		
	}
	
	public void removeOldestGames(int amountToRemove) {
	}

	public int getMAX_GAMES() {
		return MAX_GAMES;
	}
	
}
