package server.services;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import server.model.GameData;
import server.model.GameId;

@Service
public class GameManagerService {
	

	/* Using LinkedHashMap to preserve order of added elements
	 * to know which games are the oldest and can be removed if Server is overloaded
	 * */
	private Map<GameId, GameData> runningGames = new LinkedHashMap<>();
	
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
}
