package server.controller;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import server.model.GameData;
import server.model.GameId;

// Controller 
public class GameManager {
	
	/* Using LinkedHashMap to preserve order of added elements
	 * to know which games are the oldest and can be removed if Server is overloaded
	 * */
	private Map<GameId, GameData> runningGames = new LinkedHashMap<>();

	public Map<GameId, GameData> getRunningGames() {
		return Collections.unmodifiableMap(runningGames);
	}

	public void addNewGame(GameId gameId, GameData gameData) {
		runningGames.put(gameId, gameData);
	}
	
	public void removeOldGames(int amountOfGamesToRemove) {
		Iterator<Map.Entry<GameId, GameData>> mapIterator = this.getRunningGames().entrySet().iterator();
        int count = 0;
        while (mapIterator.hasNext() && count < amountOfGamesToRemove) {
            mapIterator.next();
            mapIterator.remove();
            ++count;
        }
	}

}
