package server.model;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

	/* Using LinkedHashMap to preserve order of added elements
	 * to know which games are the oldest and can be removed if Server is overloaded
	 * */
	
	private final int MAX_GAMES = 99;
	
	private Map<GameId, GameData> runningGames;
	
	private static Logger logger = LoggerFactory.getLogger(GameRepository.class);
	
	public GameRepository() {
		this.runningGames = new HashMap<GameId, GameData>();
	}
	
	public Map<GameId,GameData> getAllRunningGames() {
		return this.runningGames;
	}
	
	public GameData getRunningGameById(GameId gameId) {
		return this.getAllRunningGames().get(gameId);
	}
	
	public int getAmountOfActiveGames() {
		return 0;
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {
		this.runningGames.put(gameId, gameData);
		logger.info("New game with id {} was added", gameId);
	}
	
	public void removeOldestGames(int amountToRemove) {
	}

	public int getMAX_GAMES() {
		return MAX_GAMES;
	}
	
}
