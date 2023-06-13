package server.model;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepository {

	/* Using LinkedHashMap to preserve order of added elements
	 * to know which games are the oldest and can be removed if Server is overloaded
	 * */
		
	private Map<GameId, GameData> runningGames;
	
	private static Logger logger = LoggerFactory.getLogger(GameRepository.class);
	
	public GameRepository() {
		logger.info("NEW REPOSITORY");
		this.runningGames = new LinkedHashMap<GameId, GameData>();
	}
	
	public Map<GameId,GameData> getAllRunningGames() {
		return this.runningGames;
	}
	
	public GameData getRunningGameById(GameId gameId) {
		return this.getAllRunningGames().get(gameId);
	}
	
	public int getAmountOfActiveGames() {
		return this.runningGames.size();
	}
	
	public void addNewGame(GameId gameId, GameData gameData) {
		this.runningGames.put(gameId, gameData);
		logger.info("New game with id {} was added. Number of active games: {}", gameId, runningGames.size());
	}
	
	public void removeOldestGames(int amountToRemove) {
		Iterator<Map.Entry<GameId, GameData>> mapIterator = this.getAllRunningGames().entrySet().iterator();
        int count = 0;
        while (mapIterator.hasNext() && count < amountToRemove) {
            mapIterator.next();
            mapIterator.remove();
            ++count;
        }
	}
}
